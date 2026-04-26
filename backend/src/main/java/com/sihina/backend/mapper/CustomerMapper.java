package com.sihina.backend.mapper;

import com.sihina.backend.dto.AddressDto;
import com.sihina.backend.dto.CustomerDto;
import com.sihina.backend.dto.PhoneDto;
import com.sihina.backend.entity.Address;
import com.sihina.backend.entity.Customer;
import com.sihina.backend.entity.PhoneNumber;

import java.util.ArrayList;
import java.util.List;


/**
 * Mapper class to convert between:
 * Customer Entity - CustomerDto
 */
public class CustomerMapper {
    /**
     * convert customer entity → customerDto
     * used when sending data to frontend
     */
    public static CustomerDto toDto(Customer c) {
        if (c == null) return null;
        CustomerDto dto = new CustomerDto();

        // basic feilds
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setDateOfBirth(c.getDateOfBirth());
        dto.setNic(c.getNic());

        // phones
        List<PhoneDto> phones = new ArrayList<>();
        for (PhoneNumber p : c.getPhoneNumbers()) {
            PhoneDto pd = new PhoneDto();
            pd.setId(p.getId());
            pd.setPhone(p.getPhone());
            phones.add(pd);
        }
        dto.setPhoneNumbers(phones);

        // addresses
        List<AddressDto> addrs = new ArrayList<>();
        for (Address a : c.getAddresses()) {
            AddressDto ad = new AddressDto();
            ad.setId(a.getId());
            ad.setAddressLine1(a.getAddressLine1());
            ad.setAddressLine2(a.getAddressLine2());
            if (a.getCity() != null) ad.setCityId(a.getCity().getId());
            if (a.getCountry() != null) ad.setCountryId(a.getCountry().getId());
            addrs.add(ad);
        }
        dto.setAddresses(addrs);
        return dto;
    }
    /**
     * convert customerDto → customer entity
     * used when receiving data from frontend
     */
    public static Customer toEntity(CustomerDto dto) {
        if (dto == null) return null;
        Customer c = new Customer();

        // basic fields
        c.setId(dto.getId());
        c.setName(dto.getName());
        c.setDateOfBirth(dto.getDateOfBirth());
        c.setNic(dto.getNic());
        // phone
        if (dto.getPhoneNumbers() != null) {
            for (PhoneDto pd : dto.getPhoneNumbers()) {
                PhoneNumber p = new PhoneNumber();
                p.setPhone(pd.getPhone());
                c.addPhone(p);
            }
        }
        // addresses mapping is shallow — ensure service resolves City/Country entities by id
        if (dto.getAddresses() != null) {
            for (AddressDto ad : dto.getAddresses()) {
                Address a = new Address();
                a.setAddressLine1(ad.getAddressLine1());
                a.setAddressLine2(ad.getAddressLine2());
                c.addAddress(a);
            }
        }
        return c;
    }
}