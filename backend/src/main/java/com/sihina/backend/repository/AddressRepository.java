package com.sihina.backend.repository;



import com.sihina.backend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for Address
 * Used to perform database operations
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
    // save(), findAll(), findById(), deleteById()
}