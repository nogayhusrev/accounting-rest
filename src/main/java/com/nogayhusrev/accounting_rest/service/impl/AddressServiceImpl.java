package com.nogayhusrev.accounting_rest.service.impl;

import com.nogayhusrev.accounting_rest.client.AddressClient;
import com.nogayhusrev.accounting_rest.dto.addressApi.Country;
import com.nogayhusrev.accounting_rest.service.AddressService;
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
