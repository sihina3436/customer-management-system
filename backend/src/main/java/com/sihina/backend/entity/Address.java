package com.sihina.backend.entity;
import jakarta.persistence.*;

/**
 * JPA entity for address
 * represents the "address" table in the database
 * each address belongs to:
 *   - one Customer
 *   - one City
 *   - one Country
 */
@Entity
@Table(name="address") // Maps this entity to "address" table
public class Address {

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String addressLine1;
    private String addressLine2;

    // Many addresses can belong to one city
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="city_id")
    private City city;

    // many addresses can belong to one country
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="country_id")
    private Country country;

    // many addresses belong to one customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }
    public City getCity() { return city; }
    public void setCity(City city) { this.city = city; }
    public Country getCountry() { return country; }
    public void setCountry(Country country) { this.country = country; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}