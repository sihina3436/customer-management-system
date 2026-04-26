package com.sihina.backend.controller;
import com.sihina.backend.dto.CustomerDto;
import com.sihina.backend.service.BulkCustomerService;
import com.sihina.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:5173") // enable cors for local frontend running on port 5173
@RestController
@RequestMapping("/customers") // base url for all endpoints in this controller
@Validated
public class CustomerController {

    // server layer dependencies
    private final CustomerService customerService;
    private final BulkCustomerService bulkCustomerService;

    // constructor injection
    @Autowired
    public CustomerController(CustomerService customerService, BulkCustomerService bulkCustomerService) {
        this.customerService = customerService;
        this.bulkCustomerService = bulkCustomerService;
    }

    /**
     * create a new customer
     * endpoint: post /customers
     */
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto dto) {
        CustomerDto created = customerService.createCustomer(dto);
        return ResponseEntity.ok(created);
    }

    /**
     * update existing customer
     * endpoint: PUT /customers/{id}
     * */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDto dto) {
        CustomerDto updated = customerService.updateCustomer(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * get customer by ID
     * Endpoint: GET /customers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    /**
     * Get all customers with pagination & sorting
     * Endpoint: GET /customers
     */
    @GetMapping
    public ResponseEntity<Page<CustomerDto>> listCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(customerService.listCustomers(pageable));
    }

    /**
     * bulk upload customers via file
     * endpoint: POST /customers/bulk-upload
     * content-Type: multipart/form-data
     * supported file types:
     *   - CSV (.csv)
     *   - Excel (.xlsx)
     * Expected columns:
     *   - Name
     *   - DateOfBirth (yyyy-MM-dd)
     *   - NIC
     */
    @PostMapping(path="/bulk-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,Object>> bulkUpload(@RequestPart("file") MultipartFile file) throws Exception {
        long processed = bulkCustomerService.processBulkFile(file);
        Map<String,Object> resp = new HashMap<>();
        resp.put("processed", processed);
        resp.put("message", "Processed " + processed + " rows");
        return ResponseEntity.ok(resp);
    }
}