package com.sihina.backend.dto;
/**
 * data transfer object for address
 * used to transfer address data between frontend and backend
 */
public class AddressDto {
    private Long id;
    private String addressLine1;
    private String addressLine2;
    private Long cityId;
    private Long countryId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    public Long getCountryId() { return countryId; }
    public void setCountryId(Long countryId) { this.countryId = countryId; }
}