package com.sihina.backend.service;
import com.sihina.backend.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
/**
 * Service interface for Customer
 * Defines customer-related operations
 */
public interface CustomerService {
    // Create a new customer
    CustomerDto createCustomer(CustomerDto dto);
    // Update existing customer
    CustomerDto updateCustomer(Long id, CustomerDto dto);
    // Get customer by ID
    CustomerDto getCustomer(Long id);
    // Get all customers with pagination
    Page<CustomerDto> listCustomers(Pageable pageable);
    // Find customers by NIC list
    List<CustomerDto> findByNics(List<String> nics);
}