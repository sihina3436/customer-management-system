package com.sihina.backend.dto;
/**
 * data transfer object for city
 * used to transfer city data between backend and frontend
 */
public class CityDto {
    private Long id;
    private String name;
    private Long countryId;
    private String countryName;

    public CityDto() {}

    public CityDto(Long id, String name, Long countryId, String countryName) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
        this.countryName = countryName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getCountryId() { return countryId; }
    public void setCountryId(Long countryId) { this.countryId = countryId; }
    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
}