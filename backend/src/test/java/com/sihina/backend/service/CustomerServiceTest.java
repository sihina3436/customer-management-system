package com.sihina.backend.service;

import com.sihina.backend.dto.CustomerDto;
import com.sihina.backend.repository.CustomerRepository;

import org.testng.annotations.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testCreateAndGetCustomer() {
        CustomerDto dto = new CustomerDto();
        dto.setName("Test User");
        dto.setDateOfBirth(LocalDate.of(1990,1,1));
        dto.setNic("NIC-TEST-12345");

        CustomerDto created = customerService.createCustomer(dto);

        assertNotNull(created.getId());

        CustomerDto fetched = customerService.getCustomer(created.getId());

        assertEquals("Test User", fetched.getName());
    }
}