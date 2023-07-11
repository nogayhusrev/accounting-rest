package com.nogayhusrev.accounting_rest.controller;

import com.nogayhusrev.accounting_rest.dto.PaymentDto;
import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.entity.common.ChargeRequest;
import com.nogayhusrev.accounting_rest.service.PaymentService;
import com.nogayhusrev.accounting_rest.service.impl.StripeServiceImpl;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final StripeServiceImpl stripeServiceImpl;
    private final PaymentService paymentService;

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    public PaymentController(StripeServiceImpl stripeServiceImpl, PaymentService paymentService) {
        this.stripeServiceImpl = stripeServiceImpl;
        this.paymentService = paymentService;
    }


    @GetMapping()
    public ResponseEntity<ResponseWrapper> list() {

        int year = LocalDate.now().getYear();

        paymentService.createPaymentsIfNotExist(year);

        Map<String, Object> map = new HashMap<>();

        map.put("payments", paymentService.getAllPaymentsByYear(year));
        map.put("year", year);
        return ResponseEntity.ok(new ResponseWrapper("Payments are successfully retrieved", map, HttpStatus.OK));
    }

    @GetMapping("/{year}")
    public ResponseEntity<ResponseWrapper> list(@PathVariable String year) {

        int selectedYear = (year == null || year.isEmpty()) ? LocalDate.now().getYear() : Integer.parseInt(year);

        if (selectedYear < 2000 || selectedYear > 2100)
            throw new IllegalStateException("********* ====>>> YEAR NOT VALID <<<==== ********* ");

        paymentService.createPaymentsIfNotExist(selectedYear);

        Map<String, Object> map = new HashMap<>();

        map.put("payments", paymentService.getAllPaymentsByYear(selectedYear));
        map.put("year", selectedYear);
        return ResponseEntity.ok(new ResponseWrapper("Payments are successfully retrieved", map, HttpStatus.OK));
    }


    @GetMapping("/newPayment/{paymentId}")
    public ResponseEntity<ResponseWrapper> list(@PathVariable Long paymentId) {

        PaymentDto payment = paymentService.getPaymentById(paymentId);

        Map<String, Object> map = new HashMap<>();

        map.put("payment", payment);
        map.put("amount", payment.getAmount() * 100); //in cents
        map.put("stripePublicKey", stripePublicKey);
        map.put("currency", ChargeRequest.Currency.EUR);
        map.put("modelId", paymentId);


        return ResponseEntity.ok(new ResponseWrapper("Payment successfully retrieved", map, HttpStatus.OK));
    }


    @PostMapping("/charge/{paymentId}")
    public ResponseEntity<ResponseWrapper> charge(@RequestBody ChargeRequest chargeRequest, @PathVariable Long paymentId)
            throws StripeException {

        Map<String, Object> map = new HashMap<>();


        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);

        stripeServiceImpl.charge(chargeRequest);
        paymentService.payPayment(paymentId);

        map.put("chargedPayment", chargeRequest);


        return ResponseEntity.ok(new ResponseWrapper("Payment successfully charged", map, HttpStatus.OK));
    }


}