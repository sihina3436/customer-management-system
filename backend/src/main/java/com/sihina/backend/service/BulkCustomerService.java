package com.sihina.backend.service;

import org.springframework.web.multipart.MultipartFile;
/**
 * Service interface for bulk customer upload
 * Used to process CSV or Excel files
 */
public interface BulkCustomerService {
    /**
     * Process uploaded file (CSV or XLSX)
     * Reads customer data from file
     * Saves data into database
     */
    long processBulkFile(MultipartFile file) throws Exception;
}