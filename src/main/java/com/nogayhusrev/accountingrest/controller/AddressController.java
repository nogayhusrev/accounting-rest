package com.nogayhusrev.accountingrest.controller;


import com.nogayhusrev.accountingrest.client.AddressClient;
import com.nogayhusrev.accountingrest.dto.addressApi.CountryAndItsStatesPOSTRequest;
import com.nogayhusrev.accountingrest.dto.addressApi.CountryAndItsStatesPOSTResponse;
import com.nogayhusrev.accountingrest.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;
    private final AddressClient addressClient;

    public AddressController(AddressService addressService, AddressClient addressClient) {
        this.addressService = addressService;
        this.addressClient = addressClient;
    }

    @PostMapping()
    public ResponseEntity<CountryAndItsStatesPOSTResponse> list() {
        CountryAndItsStatesPOSTRequest countryAndItsStatesPOSTRequest = new CountryAndItsStatesPOSTRequest();
        countryAndItsStatesPOSTRequest.setCountry("Turkey");

        CountryAndItsStatesPOSTResponse countryAndItsStatesPOSTResponse = addressClient.getCountryAndItsStatesPOSTResponse(countryAndItsStatesPOSTRequest);
        return ResponseEntity.ok(countryAndItsStatesPOSTResponse);
    }


}
