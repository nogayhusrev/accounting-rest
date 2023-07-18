package com.nogayhusrev.accounting_rest.service.impl;


import com.nogayhusrev.accounting_rest.client.CurrencyClient;
import com.nogayhusrev.accounting_rest.dto.CurrencyDto;
import com.nogayhusrev.accounting_rest.dto.CurrencyResponse;
import com.nogayhusrev.accounting_rest.dto.InvoiceDto;
import com.nogayhusrev.accounting_rest.enums.InvoiceStatus;
import com.nogayhusrev.accounting_rest.enums.InvoiceType;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.service.DashboardService;
import com.nogayhusrev.accounting_rest.service.InvoiceService;
import com.nogayhusrev.accounting_rest.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final CurrencyClient currencyClient;
    private final InvoiceService invoiceService;
    private final UserService userService;

    public DashboardServiceImpl(CurrencyClient currencyClient, InvoiceService invoiceService, UserService userService) {
        this.currencyClient = currencyClient;
        this.invoiceService = invoiceService;
        this.userService = userService;
    }

    @Override
    public Map<String, BigDecimal> getSummaryNumbers() throws AccountingProjectException {

        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");


        Map<String, BigDecimal> summaryNumbersMap = new HashMap<>();
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal profitLoss = BigDecimal.ZERO;
        List<InvoiceDto> allApprovedInvoicesOfCompany = invoiceService.findInvoiceByInvoiceStatus(InvoiceStatus.APPROVED);
        for (InvoiceDto invoice : allApprovedInvoicesOfCompany) {
            if (invoice.getInvoiceType() == InvoiceType.PURCHASE) {
                totalCost = totalCost.add(invoiceService.getTotalPriceOfInvoice(invoice.getId())).add(invoiceService.getTotalTaxOfInvoice(invoice.getId()));
            } else {
                totalSales = totalSales.add(invoiceService.getTotalPriceOfInvoice(invoice.getId())).add(invoiceService.getTotalTaxOfInvoice(invoice.getId()));
                profitLoss = profitLoss.add(invoiceService.getProfitLossOfInvoice(invoice.getId()));
            }
        }
        summaryNumbersMap.put("totalCost", totalCost);
        summaryNumbersMap.put("totalSales", totalSales);
        summaryNumbersMap.put("profitLoss", profitLoss);
        return summaryNumbersMap;
    }


    @Override
    public CurrencyDto getExchangeRates() throws AccountingProjectException {


        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");


        CurrencyResponse currency = currencyClient.getUsdBasedCurrencies();
        CurrencyDto currencyDto = CurrencyDto.builder()
                .euro(currency.getUsd().getEur())
                .britishPound(currency.getUsd().getGbp())
                .indianRupee(currency.getUsd().getInr())
                .japaneseYen(currency.getUsd().getJpy())
                .canadianDollar(currency.getUsd().getCad())
                .build();


        return currencyDto;
    }

    private boolean isNotAdminOrManager() {
        return !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Manager")
                &&
                !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Employee");
    }
}
