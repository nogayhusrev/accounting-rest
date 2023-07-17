package com.nogayhusrev.accounting_rest.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChargeRequest {
    private String description;
    private int amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;

    public enum Currency {
        EUR, USD
    }
}