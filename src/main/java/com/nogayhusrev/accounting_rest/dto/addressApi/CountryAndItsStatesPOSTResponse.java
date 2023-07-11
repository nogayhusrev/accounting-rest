package com.nogayhusrev.accounting_rest.dto.addressApi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import javax.validation.Valid;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "error",
        "msg",
        "data"
})
@Generated("jsonschema2pojo")
public class CountryAndItsStatesPOSTResponse {

    @JsonProperty("error")
    public Boolean error;
    @JsonProperty("msg")
    public String msg;
    @JsonProperty("data")
    @Valid
    public Country country;

}
