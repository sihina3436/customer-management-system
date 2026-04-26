package com.sihina.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * JPA entity for customer
 * represents the "customer" table in the database
 * stores personal details and relationships (phones, addresses, family)
 */
@Entity
@Table(name = "customer", uniqueConstraints = @UniqueConstraint(columnNames = {"nic"}))
public class Customer {

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(name="date_of_birth", nullable=false)
    private LocalDate dateOfBirth;

    @Column(nullable=false)
    private String nic;

    // timestamp when record is created
    @Column(name="created_at", insertable=true, updatable=false)
    private LocalDateTime createdAt;

    // timestamp when record is updated
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    // One customer can have many phone numbers
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    // family relations where this customer is the owner
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FamilyRelation> familyRelations = new HashSet<>();

    // convenience methods
    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = createdAt; }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public void addPhone(PhoneNumber p) {
        phoneNumbers.add(p);
        p.setCustomer(this);
    }
    public void removePhone(PhoneNumber p) {
        phoneNumbers.remove(p);
        p.setCustomer(null);
    }
    public void addAddress(Address a) {
        addresses.add(a);
        a.setCustomer(this);
    }
    public void removeAddress(Address a) {
        addresses.remove(a);
        a.setCustomer(null);
    }
    public void addFamilyRelation(FamilyRelation fr) {
        familyRelations.add(fr);
        fr.setCustomer(this);
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<PhoneNumber> getPhoneNumbers() { return phoneNumbers; }
    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) { this.phoneNumbers = phoneNumbers; }
    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
    public Set<FamilyRelation> getFamilyRelations() { return familyRelations; }
    public void setFamilyRelations(Set<FamilyRelation> familyRelations) { this.familyRelations = familyRelations; }
}