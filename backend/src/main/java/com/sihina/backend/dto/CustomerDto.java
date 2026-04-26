package com.sihina.backend.dto;
/**
 * data transfer object for Customer
 * used to transfer customer data between frontend and backend
 */
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class CustomerDto {
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate dateOfBirth;

    @NotBlank
    private String nic;

    private List<PhoneDto> phoneNumbers;
    private List<AddressDto> addresses;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }
    public List<PhoneDto> getPhoneNumbers() { return phoneNumbers; }
    public void setPhoneNumbers(List<PhoneDto> phoneNumbers) { this.phoneNumbers = phoneNumbers; }
    public List<AddressDto> getAddresses() { return addresses; }
    public void setAddresses(List<AddressDto> addresses) { this.addresses = addresses; }
}