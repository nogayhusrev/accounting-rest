package com.nogayhusrev.accounting_rest.service;

import com.nogayhusrev.accounting_rest.dto.PaymentDto;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;

import java.util.List;

public interface PaymentService {

    List<PaymentDto> getAllPaymentsByYear(int year) throws AccountingProjectException;

    void createPaymentsIfNotExist(int year) throws AccountingProjectException;

    PaymentDto getPaymentById(Long id) throws AccountingProjectException;

    PaymentDto payPayment(Long id) throws AccountingProjectException;
}
