package com.nogayhusrev.accountingrest.service.impl;

import com.nogayhusrev.accountingrest.dto.CompanyDto;
import com.nogayhusrev.accountingrest.dto.PaymentDto;
import com.nogayhusrev.accountingrest.entity.Company;
import com.nogayhusrev.accountingrest.entity.Payment;
import com.nogayhusrev.accountingrest.enums.Months;
import com.nogayhusrev.accountingrest.mapper.MapperUtil;
import com.nogayhusrev.accountingrest.repository.PaymentRepository;
import com.nogayhusrev.accountingrest.service.PaymentService;
import com.nogayhusrev.accountingrest.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final MapperUtil mapperUtil;
    private final UserService userService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, MapperUtil mapperUtil, UserService userService) {
        this.paymentRepository = paymentRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
    }

    @Override
    public List<PaymentDto> getAllPaymentsByYear(int year) {

        LocalDate instance = LocalDate.now().withYear(year);
        LocalDate dateStart = instance.with(firstDayOfYear());
        LocalDate dateEnd = instance.with(lastDayOfYear());
        var company = userService.getCurrentUser().getCompany();

        List<Payment> payments = paymentRepository.findAllByYearBetweenAndCompanyId(dateStart, dateEnd, company.getId());
        return payments.stream()
                .map(obj -> mapperUtil.convert(obj, new PaymentDto()))
                .sorted(Comparator.comparing(PaymentDto::getMonth))
                .collect(Collectors.toList());
    }

    @Override
    public void createPaymentsIfNotExist(int year) {

        LocalDate instance = LocalDate.now().withYear(year);
        LocalDate dateStart = instance.with(firstDayOfYear());
        LocalDate dateEnd = instance.with(lastDayOfYear());
        CompanyDto companyDto = userService.getCurrentUser().getCompany();

        List<Payment> payments = paymentRepository.findAllByYearBetweenAndCompanyId(dateStart, dateEnd, companyDto.getId());


        if (payments.size() == 0) {
            for (Months month : Months.values()) {
                Payment payment = new Payment();
                payment.setMonth(month);
                payment.setYear(LocalDate.now().withYear(year));
                payment.setPaid(false);
                payment.setAmount(250);
                payment.setCompany(mapperUtil.convert(companyDto, new Company()));
                paymentRepository.save(payment);
            }
        }

    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id).get();
        return mapperUtil.convert(payment, new PaymentDto());
    }

    @Override
    public PaymentDto payPayment(Long id) {

        Payment payment = paymentRepository.findById(id).get();
        payment.setPaid(true);
        return mapperUtil.convert(paymentRepository.save(payment), new PaymentDto());
    }


}