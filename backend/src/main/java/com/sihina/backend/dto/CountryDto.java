package com.sihina.backend.dto;
/**
 * data transfer object for country
 * used to transfer country data between backend and frontend
 */
public class CountryDto {
    private Long id;
    private String name;
    private String isoCode;

    public CountryDto() {}

    public CountryDto(Long id, String name, String isoCode) {
        this.id = id;
        this.name = name;
        this.isoCode = isoCode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIsoCode() { return isoCode; }
    public void setIsoCode(String isoCode) { this.isoCode = isoCode; }
}