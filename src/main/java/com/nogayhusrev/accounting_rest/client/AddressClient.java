package com.nogayhusrev.accounting_rest.client;


import com.nogayhusrev.accounting_rest.dto.addressApi.CountriesAndStatesResponse;
import com.nogayhusrev.accounting_rest.dto.addressApi.CountryAndItsStatesPOSTRequest;
import com.nogayhusrev.accounting_rest.dto.addressApi.CountryAndItsStatesPOSTResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "address-api", url = "https://countriesnow.space/api/v0.1/countries")
public interface AddressClient {


    @GetMapping( "/states")
    CountriesAndStatesResponse getCountriesAndStatesGETResponse();

    @PostMapping("/states")
    CountryAndItsStatesPOSTResponse getCountriesAndStatesPOSTResponse(@RequestBody CountryAndItsStatesPOSTRequest body);






}
