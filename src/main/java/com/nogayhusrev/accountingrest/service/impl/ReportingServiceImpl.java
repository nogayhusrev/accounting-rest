package com.nogayhusrev.accountingrest.service.impl;


import com.nogayhusrev.accountingrest.dto.InvoiceProductDto;
import com.nogayhusrev.accountingrest.entity.InvoiceProduct;
import com.nogayhusrev.accountingrest.enums.InvoiceStatus;
import com.nogayhusrev.accountingrest.enums.InvoiceType;
import com.nogayhusrev.accountingrest.mapper.MapperUtil;
import com.nogayhusrev.accountingrest.service.InvoiceProductService;
import com.nogayhusrev.accountingrest.service.ReportingService;
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

    public ReportingServiceImpl(InvoiceProductService invoiceProductService, MapperUtil mapperUtil) {
        this.invoiceProductService = invoiceProductService;
        this.mapperUtil = mapperUtil;
    }


    @Override
    public List<InvoiceProductDto> getStock() {
        return invoiceProductService.findAll().stream()
                .filter(invoiceProductDto -> invoiceProductDto.getInvoice().getInvoiceStatus().equals(InvoiceStatus.APPROVED))
                .sorted(Comparator.comparing(InvoiceProductDto::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, BigDecimal> getProfitLoss() {
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


}
