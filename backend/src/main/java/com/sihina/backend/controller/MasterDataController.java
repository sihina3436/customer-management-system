package com.sihina.backend.controller;

import com.sihina.backend.entity.City;
import com.sihina.backend.entity.Country;
import com.sihina.backend.service.MasterDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sihina.backend.dto.CityDto;
import com.sihina.backend.dto.CountryDto;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173") // enable cors for local frontend running on port 5173
@RestController
@RequestMapping("/master") // base url for all endpoints in this controller
public class MasterDataController {

    // server layer dependencies
    private final MasterDataService masterDataService;

    // constructor injection
    @Autowired
    public MasterDataController(MasterDataService masterDataService) { this.masterDataService = masterDataService; }

    /**
     * Get all countries
     * Endpoint: GET /master/countries
     */
    @GetMapping("/countries")
    public List<CountryDto> countries() { return masterDataService.listCountries(); }

    /**
     * Get all cities
     * Endpoint: GET /master/cities
     */
    @GetMapping("/cities")
    public List<CityDto> cities() { return masterDataService.listCities(); }
}