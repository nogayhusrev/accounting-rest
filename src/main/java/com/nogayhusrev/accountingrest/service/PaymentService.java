package com.nogayhusrev.accountingrest.service;

import com.nogayhusrev.accountingrest.dto.PaymentDto;

import java.util.List;

public interface PaymentService {

    List<PaymentDto> getAllPaymentsByYear(int year);

    void createPaymentsIfNotExist(int year);

    PaymentDto getPaymentById(Long id);

    PaymentDto payPayment(Long id);
}
