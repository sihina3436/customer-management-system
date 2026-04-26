package com.sihina.backend.service;

import com.sihina.backend.dto.CityDto;
import com.sihina.backend.dto.CountryDto;

import java.util.List;
/**
 * Service interface for master data
 * Used to get Country and City data
 */
public interface MasterDataService {
    // Get all countries
    List<CountryDto> listCountries();
    // Get all cities
    List<CityDto> listCities();
}