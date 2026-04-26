package com.sihina.backend.service.impl;

import com.sihina.backend.service.BulkCustomerService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class BulkCustomerServiceImpl implements BulkCustomerService {
    // logger for printing error messages
    private static final Logger LOGGER = LoggerFactory.getLogger(BulkCustomerServiceImpl.class);
    // used to run SQL queries directly
    private final JdbcTemplate jdbcTemplate;
    // number of records inserted at once
    private final int BATCH_SIZE = 1000;

    @Autowired
    public BulkCustomerServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    /**
     * main method to process uploaded file
     * Supports CSV and Excel (.xlsx)
     */
    @Override
    public long processBulkFile(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        if (filename == null) throw new IllegalArgumentException("Empty filename");
        // Check file type and call correct method
        if (filename.toLowerCase().endsWith(".csv")) {
            return processCsv(file.getInputStream());
        } else if (filename.toLowerCase().endsWith(".xlsx")) {
            return processXlsx(file.getInputStream());
        } else {
            throw new IllegalArgumentException("Unsupported file type. Accepts .csv or .xlsx");
        }
    }

    /**
     * Process CSV file
     */
    private long processCsv(InputStream is) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("Name", "DateOfBirth", "NIC")
                    .setSkipHeaderRecord(true)
                    .setTrim(true)
                    .build();

            // Define CSV format
            Iterable<CSVRecord> records = csvFormat.parse(reader);
            List<Object[]> batchArgs = new ArrayList<>(BATCH_SIZE);
            long processed = 0;

            for (CSVRecord r : records) {
                String name = r.isMapped("Name") ? r.get("Name") : null;
                String dob = r.isMapped("DateOfBirth") ? r.get("DateOfBirth") : null;
                String nic = r.isMapped("NIC") ? r.get("NIC") : null;

                // Skip invalid rows
                if (name == null || name.trim().isEmpty()) continue;
                if (dob == null || nic == null) continue;

                try {
                    LocalDate.parse(dob.trim()); // validate format
                } catch (DateTimeParseException ex) {
                    LOGGER.warn("Skipping row with invalid date: {}", dob);
                    continue;
                }

                batchArgs.add(new Object[]{name.trim(), dob.trim(), nic.trim()});
                if (batchArgs.size() >= BATCH_SIZE) {
                    executeBatch(batchArgs);
                    processed += batchArgs.size();
                    batchArgs.clear();
                }
            }

            if (!batchArgs.isEmpty()) {
                executeBatch(batchArgs);
                processed += batchArgs.size();
            }
            return processed;
        }
    }
    /**
     * Execute batch insert query
     */
    private void executeBatch(List<Object[]> batchArgs) {
        String sql = "INSERT INTO customer (name, date_of_birth, nic) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE name=VALUES(name), date_of_birth=VALUES(date_of_birth)";
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    /**
     * Process Excel (.xlsx) file
     */
    private long processXlsx(InputStream is) throws Exception {
        File tmp = File.createTempFile("upload", ".xlsx");
        tmp.deleteOnExit();
        try (OutputStream out = new FileOutputStream(tmp)) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = is.read(buf)) != -1) out.write(buf, 0, r);
        }

        // Read Excel file using Apache POI
        try (OPCPackage pkg = OPCPackage.open(tmp)) {
            XSSFReader reader = new XSSFReader(pkg);
            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) reader.getSheetsData();
            long processed = 0;
            while (iter.hasNext()) {
                try (InputStream sheetStream = iter.next()) {
                    processed += processSheet(sheetStream);
                }
            }
            return processed;
        }
    }
    /**
     * Process each Excel sheet using SAX parser
     */
    private long processSheet(InputStream sheetInputStream) throws Exception {
        SheetHandler handler = new SheetHandler();
        try {
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            saxFactory.setNamespaceAware(false);
            XMLReader parser = saxFactory.newSAXParser().getXMLReader();
            parser.setContentHandler(handler);
            InputSource sheetSource = new InputSource(sheetInputStream);
            parser.parse(sheetSource);
        } catch (SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return handler.getProcessedCount();
    }

    // Lightweight SAX handler for simple XLSX sheets where header row contains Name, DateOfBirth, NIC
    private class SheetHandler extends DefaultHandler {
        private boolean vIsOpen = false;
        private StringBuilder lastContents = new StringBuilder();
        private final List<Object[]> batch = new ArrayList<>(BATCH_SIZE);
        private int processed = 0;
        private boolean isHeader = true;
        private final Map<Integer, String> headerMap = new HashMap<>();
        private int currentCol = -1;
        private final Map<Integer, String> currentRowValues = new HashMap<>();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if ("c".equals(qName)) {
                String r = attributes.getValue("r");
                currentCol = nameToColumn(r);
            } else if ("v".equals(qName) || "t".equals(qName)) {
                vIsOpen = true;
                lastContents.setLength(0);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (vIsOpen && ("v".equals(qName) || "t".equals(qName))) {
                vIsOpen = false;
                String value = lastContents.toString().trim();
                if (currentCol >= 0) currentRowValues.put(currentCol, value);
            } else if ("row".equals(qName)) {
                if (isHeader) {
                    // capture header names (by column index)
                    headerMap.putAll(currentRowValues);
                    isHeader = false;
                } else {
                    String nameVal = findByHeader("Name");
                    String dob = findByHeader("DateOfBirth");
                    String nic = findByHeader("NIC");

                    if (nameVal != null && !nameVal.isEmpty() && dob != null && nic != null && !nic.isEmpty()) {
                        try {
                            LocalDate.parse(dob.trim());
                            batch.add(new Object[]{nameVal.trim(), dob.trim(), nic.trim()});
                            if (batch.size() >= BATCH_SIZE) {
                                executeBatch(new ArrayList<>(batch));
                                processed += batch.size();
                                batch.clear();
                            }
                        } catch (Exception ex) {
                            LOGGER.warn("Skipping invalid row with dob: {}", dob);
                        }
                    }
                }
                currentRowValues.clear();
            }
        }

        private String findByHeader(String headerName) {
            for (Map.Entry<Integer, String> e : headerMap.entrySet()) {
                if (headerName.equalsIgnoreCase(e.getValue())) {
                    return currentRowValues.getOrDefault(e.getKey(), null);
                }
            }
            return null;
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (vIsOpen) lastContents.append(ch, start, length);
        }

        public int getProcessedCount() {
            if (!batch.isEmpty()) {
                executeBatch(batch);
                processed += batch.size();
                batch.clear();
            }
            return processed;
        }

        private int nameToColumn(String name) {
            if (name == null) return -1;
            int col = -1;
            for (int i = 0; i < name.length(); ++i) {
                char ch = name.charAt(i);
                if (Character.isDigit(ch)) break;
                col = (col + 1) * 26 + (ch - 'A');
            }
            return col;
        }
    }
}