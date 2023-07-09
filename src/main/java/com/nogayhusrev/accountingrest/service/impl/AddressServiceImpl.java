package com.nogayhusrev.accountingrest.service.impl;

import com.nogayhusrev.accountingrest.client.AddressClient;
import com.nogayhusrev.accountingrest.dto.addressApi.Country;
import com.nogayhusrev.accountingrest.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressClient addressClient;

    public AddressServiceImpl(AddressClient addressClient) {
        this.addressClient = addressClient;
    }


    @Override
    public List<Country> getAllCountries() {

        List<Country> countries = addressClient.getCountriesAndStatesGETResponse().getCountries();

        countries = countries.stream().sorted(Comparator.comparing(country -> country.name)).collect(Collectors.toList());

        return countries;
    }

}
