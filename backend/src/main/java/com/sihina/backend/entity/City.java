package com.sihina.backend.entity;
import jakarta.persistence.*;

/**
 * JPA entity for city
 * represents the "city" table in the database
 */
@Entity
@Table(name = "city", uniqueConstraints = @UniqueConstraint(columnNames = {"name","country_id"}))
// ensure same city name cannot exist twice within the same country
public class City {
    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    // many cities belong to one country
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="country_id", nullable=false)
    private Country country;


    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
