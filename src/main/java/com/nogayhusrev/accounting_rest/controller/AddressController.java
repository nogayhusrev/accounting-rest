package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.client.AddressClient;
import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.dto.addressApi.CountriesAndStatesResponse;
import com.nogayhusrev.accounting_rest.dto.addressApi.CountryAndItsStatesPOSTRequest;
import com.nogayhusrev.accounting_rest.dto.addressApi.CountryAndItsStatesPOSTResponse;
import com.nogayhusrev.accounting_rest.service.AddressService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController()
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;
    private final AddressClient addressClient;

    private final RestTemplate restTemplate;

    public AddressController(AddressService addressService, AddressClient addressClient, RestTemplate restTemplate) {
        this.addressService = addressService;
        this.addressClient = addressClient;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getCountries() throws Exception {
        CountriesAndStatesResponse countriesAndStatesResponse = addressClient.getCountriesAndStatesGETResponse();
        return ResponseEntity.ok(new ResponseWrapper("Countries and States are successfully retrieved", countriesAndStatesResponse, HttpStatus.OK));
    }


    @GetMapping("/list")
    public ResponseEntity<CountryAndItsStatesPOSTResponse> getCountryAndStates() {
        // Set the request URL
        String url = "https://countriesnow.space/api/v0.1/countries/states";

        // Set the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Set the request body
        CountryAndItsStatesPOSTRequest requestBody = new CountryAndItsStatesPOSTRequest();
        requestBody.setCountry("Turkey");

        HttpEntity<CountryAndItsStatesPOSTRequest> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send the POST request
        ResponseEntity<CountryAndItsStatesPOSTResponse> response = restTemplate.postForEntity(url, requestEntity, CountryAndItsStatesPOSTResponse.class);

        // Process the response
       return response;
    }
}






