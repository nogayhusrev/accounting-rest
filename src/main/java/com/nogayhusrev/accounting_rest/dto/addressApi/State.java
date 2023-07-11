package com.nogayhusrev.accounting_rest.dto.addressApi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "state_code"
})
@Generated("jsonschema2pojo")
public class State {

    @JsonProperty("name")
    public String name;
    @JsonProperty("state_code")
    public String stateCode;

}
