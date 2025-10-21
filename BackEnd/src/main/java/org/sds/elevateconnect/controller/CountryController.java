package org.sds.elevateconnect.controller;

import org.sds.elevateconnect.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping("/countries")
    public String[] getAllCountries() {
        return countryService.getAllCountries();
    }
}
