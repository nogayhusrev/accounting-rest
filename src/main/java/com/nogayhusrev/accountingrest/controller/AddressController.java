package com.nogayhusrev.accountingrest.controller;


import com.nogayhusrev.accountingrest.client.AddressClient;
import com.nogayhusrev.accountingrest.dto.ResponseWrapper;
import com.nogayhusrev.accountingrest.dto.addressApi.CountriesAndStatesResponse;
import com.nogayhusrev.accountingrest.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;
    private final AddressClient addressClient;

    public AddressController(AddressService addressService, AddressClient addressClient) {
        this.addressService = addressService;
        this.addressClient = addressClient;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getCountries() throws Exception {
        CountriesAndStatesResponse countriesAndStatesResponse = addressClient.getCountriesAndStatesGETResponse();
        return ResponseEntity.ok(new ResponseWrapper("Countries and States are successfully retrieved", countriesAndStatesResponse, HttpStatus.OK));
    }




}
