package com.sihina.backend.repository;
import com.sihina.backend.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for PhoneNumber
 * Used to access phone number data from database
 */
public interface PhoneRepository extends JpaRepository<PhoneNumber, Long> {
    // save(), findAll(), findById(), deleteById()
}