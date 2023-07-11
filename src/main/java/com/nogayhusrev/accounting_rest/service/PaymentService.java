package com.nogayhusrev.accounting_rest.service;

import com.nogayhusrev.accounting_rest.dto.PaymentDto;

import java.util.List;

public interface PaymentService {

    List<PaymentDto> getAllPaymentsByYear(int year);

    void createPaymentsIfNotExist(int year);

    PaymentDto getPaymentById(Long id);

    PaymentDto payPayment(Long id);
}
