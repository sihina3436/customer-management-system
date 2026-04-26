package com.sihina.backend.entity;

import jakarta.persistence.*;

/**
 * JPA entity for familyRelation
 * represents relationships between customers
 */

@Entity
@Table(name="family_relation", uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id","family_member_id"}))
public class FamilyRelation {
    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;

    // family member (also customer)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="family_member_id", nullable=false)
    private Customer familyMember;

    // type of relationship (father , mother)
    private String relationType;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Customer getFamilyMember() { return familyMember; }
    public void setFamilyMember(Customer familyMember) { this.familyMember = familyMember; }
    public String getRelationType() { return relationType; }
    public void setRelationType(String relationType) { this.relationType = relationType; }
}