package com.sihina.backend.entity;
import jakarta.persistence.*;

/**
 * JPA entity for country
 * represents the "country" table in the database
 * stores country information
 * ensures each country name is unique
 */
@Entity
@Table(name = "country", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Country {
    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    // iso code ( eg: LK, UK, USA)
    private String isoCode;

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

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }
}