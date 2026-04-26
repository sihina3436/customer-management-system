package com.sihina.backend.repository;
import com.sihina.backend.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for City
 * Used to perform database operations for City table
 */
public interface CityRepository extends JpaRepository<City, Long> {
    // save(), findAll(), findById(), deleteById()
}
