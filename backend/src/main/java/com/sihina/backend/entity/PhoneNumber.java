package com.sihina.backend.entity;
import jakarta.persistence.*;

/**
 * JPA entity for phoneNumber
 * represents the "phone_number" table in the database
 * stores phone numbers linked to a customer
 */

@Entity
@Table(name="phone_number")
public class PhoneNumber {
    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    // Many phone numbers belong to one customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}