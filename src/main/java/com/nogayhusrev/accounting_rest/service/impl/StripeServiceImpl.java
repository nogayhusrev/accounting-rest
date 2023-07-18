package com.nogayhusrev.accounting_rest.service.impl;

import com.nogayhusrev.accounting_rest.entity.common.ChargeRequest;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeServiceImpl {

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    private final UserService userService;

    public StripeServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public Charge charge(ChargeRequest chargeRequest) throws StripeException, AccountingProjectException {
        if (isNotAdmin())
            throw new AccountingProjectException("You Are Not Admin");


        Map<String, Object> chargeParams = new HashMap<>();

        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());

        return Charge.create(chargeParams);
    }

    private boolean isNotAdmin() {
        return !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Admin");
    }
}