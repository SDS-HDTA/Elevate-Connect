package org.sds.elevateconnect.service;

import org.sds.elevateconnect.mapper.CountryMapper;
import org.sds.elevateconnect.service.interfaces.ICountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CountryService implements ICountryService{
    @Autowired
    private CountryMapper countryMapper;

    @Override
    public String[] getAllCountries() {
        return countryMapper.getAllCountries();
    }
}
