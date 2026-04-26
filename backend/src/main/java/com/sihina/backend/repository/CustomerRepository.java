package com.sihina.backend.repository;

import com.sihina.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Repository for Customer
 * Used to access Customer data from database
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Find a customer by NIC
    Optional<Customer> findByNic(String nic);
    // Check if a customer already exists with given NIC
    boolean existsByNic(String nic);
}