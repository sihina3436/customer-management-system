package com.sihina.backend.repository;

import com.sihina.backend.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for Country
 * Used to access Country table in database
 */
public interface CountryRepository extends JpaRepository<Country, Long> {
    // save(), findAll(), findById(), deleteById()
}