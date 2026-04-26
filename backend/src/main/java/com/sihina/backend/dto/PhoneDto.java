package com.sihina.backend.dto;

/**
 * data transfer object for Phone
 * used to transfer phone number data between frontend and backend
 */
public class PhoneDto {
    private Long id;
    private String phone;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}