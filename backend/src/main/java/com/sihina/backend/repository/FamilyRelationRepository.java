package com.sihina.backend.repository;
import com.sihina.backend.entity.FamilyRelation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for FamilyRelation
 * Used to access family relationship data
 */
public interface FamilyRelationRepository extends JpaRepository<FamilyRelation, Long> {
    // save(), findAll(), findById(), deleteById()
}