package com.sihina.backend.service.impl;

import com.sihina.backend.dto.CityDto;
import com.sihina.backend.dto.CountryDto;
import com.sihina.backend.entity.City;
import com.sihina.backend.entity.Country;
import com.sihina.backend.repository.CityRepository;
import com.sihina.backend.repository.CountryRepository;
import com.sihina.backend.service.MasterDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Service for master data (Country & City)
 * Used to fetch data for dropdowns in frontend
 */
@Service
public class MasterDataServiceImpl implements MasterDataService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    @Autowired
    public MasterDataServiceImpl(CountryRepository countryRepository, CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    /**
     * Get all countries
     */
    @Override
    @Transactional(readOnly = true)
    public List<CountryDto> listCountries() {
        List<Country> countries = countryRepository.findAll();
        return countries.stream()
                .map(c -> new CountryDto(c.getId(), c.getName(), c.getIsoCode()))
                .collect(Collectors.toList());
    }
    /**
     * Get all cities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CityDto> listCities() {
        List<City> cities = cityRepository.findAll();
        return cities.stream()
                .map(c -> {
                    Long countryId = null;
                    String countryName = null;
                    if (c.getCountry() != null) {
                        countryId = c.getCountry().getId();
                        countryName = c.getCountry().getName();
                    }
                    return new CityDto(c.getId(), c.getName(), countryId, countryName);
                })
                .collect(Collectors.toList());
    }
}