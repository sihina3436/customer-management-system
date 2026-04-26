package com.sihina.backend.service.impl;

import com.sihina.backend.dto.AddressDto;
import com.sihina.backend.dto.CustomerDto;
import com.sihina.backend.entity.*;
import com.sihina.backend.mapper.CustomerMapper;
import com.sihina.backend.repository.AddressRepository;
import com.sihina.backend.repository.CityRepository;
import com.sihina.backend.repository.CountryRepository;
import com.sihina.backend.repository.CustomerRepository;
import com.sihina.backend.exception.ResourceNotFoundException;
import com.sihina.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    // Repositories for database operations
    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CityRepository cityRepository,
                               CountryRepository countryRepository,
                               AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * Create new customer
     */
    @Override
    @Transactional
    public CustomerDto createCustomer(CustomerDto dto) {
        if (customerRepository.existsByNic(dto.getNic())) {
            throw new IllegalArgumentException("Customer with NIC already exists: " + dto.getNic());
        }
        Customer entity = CustomerMapper.toEntity(dto);

        if (dto.getAddresses() != null) {
            entity.getAddresses().clear();
            for (AddressDto ad : dto.getAddresses()) {
                Address a = new Address();
                a.setAddressLine1(ad.getAddressLine1());
                a.setAddressLine2(ad.getAddressLine2());
                if (ad.getCityId() != null) {
                    City city = cityRepository.findById(ad.getCityId())
                            .orElseThrow(() -> new ResourceNotFoundException("City not found: " + ad.getCityId()));
                    a.setCity(city);
                }
                if (ad.getCountryId() != null) {
                    Country country = countryRepository.findById(ad.getCountryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Country not found: " + ad.getCountryId()));
                    a.setCountry(country);
                }
                entity.addAddress(a);
            }
        }
        Customer saved = customerRepository.save(entity);
        return CustomerMapper.toDto(saved);
    }
    /**
     * Update existing customer
     */
    @Override
    @Transactional
    public CustomerDto updateCustomer(Long id, CustomerDto dto) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        // Only update allowed fields
        existing.setName(dto.getName());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setNic(dto.getNic());
        // phones: replace
        existing.getPhoneNumbers().clear();
        if (dto.getPhoneNumbers() != null) {
            dto.getPhoneNumbers().forEach(pd -> {
                PhoneNumber p = new PhoneNumber();
                p.setPhone(pd.getPhone());
                existing.addPhone(p);
            });
        }
        // addresses: replace and resolve
        existing.getAddresses().clear();
        if (dto.getAddresses() != null) {
            for (AddressDto ad : dto.getAddresses()) {
                Address a = new Address();
                a.setAddressLine1(ad.getAddressLine1());
                a.setAddressLine2(ad.getAddressLine2());
                if (ad.getCityId() != null) {
                    City city = cityRepository.findById(ad.getCityId())
                            .orElseThrow(() -> new ResourceNotFoundException("City not found: " + ad.getCityId()));
                    a.setCity(city);
                }
                if (ad.getCountryId() != null) {
                    Country country = countryRepository.findById(ad.getCountryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Country not found: " + ad.getCountryId()));
                    a.setCountry(country);
                }
                existing.addAddress(a);
            }
        }
        Customer saved = customerRepository.save(existing);
        return CustomerMapper.toDto(saved);
    }
    /**
     * Get customer by ID
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomer(Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        return CustomerMapper.toDto(c);
    }

    /**
     * Get all customers (with pagination)
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> listCustomers(Pageable pageable) {
        Page<Customer> p = customerRepository.findAll(pageable);
        List<CustomerDto> dtos = new ArrayList<>();
        for (Customer c : p.getContent()) dtos.add(CustomerMapper.toDto(c));
        return new PageImpl<>(dtos, pageable, p.getTotalElements());
    }
    /**
     * Find customers by NIC list
     */
    @Override
    public List<CustomerDto> findByNics(List<String> nics) {
        // simple implementation: query individually (could be optimized with custom repository)
        List<CustomerDto> results = new ArrayList<>();
        for (String nic : nics) {
            customerRepository.findByNic(nic).ifPresent(c -> results.add(CustomerMapper.toDto(c)));
        }
        return results;
    }
}