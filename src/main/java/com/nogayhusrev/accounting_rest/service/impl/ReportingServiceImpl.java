package com.nogayhusrev.accounting_rest.service.impl;


import com.nogayhusrev.accounting_rest.dto.InvoiceProductDto;
import com.nogayhusrev.accounting_rest.entity.InvoiceProduct;
import com.nogayhusrev.accounting_rest.enums.InvoiceStatus;
import com.nogayhusrev.accounting_rest.enums.InvoiceType;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.mapper.MapperUtil;
import com.nogayhusrev.accounting_rest.service.InvoiceProductService;
import com.nogayhusrev.accounting_rest.service.ReportingService;
import com.nogayhusrev.accounting_rest.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportingServiceImpl implements ReportingService {

    private final InvoiceProductService invoiceProductService;

    private final MapperUtil mapperUtil;

    private final UserService userService;

    public ReportingServiceImpl(InvoiceProductService invoiceProductService, MapperUtil mapperUtil, UserService userService) {
        this.invoiceProductService = invoiceProductService;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
    }


    @Override
    public List<InvoiceProductDto> getStock() throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        return invoiceProductService.findAll().stream()
                .filter(invoiceProductDto -> invoiceProductDto.getInvoice().getInvoiceStatus().equals(InvoiceStatus.APPROVED))
                .sorted(Comparator.comparing(InvoiceProductDto::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, BigDecimal> getProfitLoss() throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Map<String, BigDecimal> profitLossMap = new HashMap<>();

        List<InvoiceProduct> salesInvoiceProducts = invoiceProductService.findInvoiceProductsByInvoiceType(InvoiceType.SALES);

        for (InvoiceProduct invoiceProduct : salesInvoiceProducts) {
            int year = invoiceProduct.getInvoice().getDate().getYear();
            String month = invoiceProduct.getInvoice().getDate().getMonth().toString();
            BigDecimal profitLoss = invoiceProduct.getProfitLoss();
            String timeWindow = year + " " + month;
            profitLossMap.put(timeWindow, profitLossMap.getOrDefault(timeWindow, BigDecimal.ZERO).add(profitLoss));
        }
        return profitLossMap;
    }

    private boolean isNotAdminOrManager() {
        return !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Manager")
                &&
                !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Employee");
    }

}
