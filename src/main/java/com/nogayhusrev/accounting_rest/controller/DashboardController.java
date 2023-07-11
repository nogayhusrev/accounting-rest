package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.service.CompanyService;
import com.nogayhusrev.accounting_rest.service.DashboardService;
import com.nogayhusrev.accounting_rest.service.InvoiceService;
import com.nogayhusrev.accounting_rest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class DashboardController {

    private final InvoiceService invoiceService;
    private final DashboardService dashboardService;
    private final UserService userService;


    public DashboardController(InvoiceService invoiceService, DashboardService dashboardService, CompanyService companyService, UserService userService) {
        this.invoiceService = invoiceService;
        this.dashboardService = dashboardService;

        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<ResponseWrapper> getDashBoard() {

        Map<String, Object> map = new HashMap<>();

        map.put("companyTitle", userService.getCurrentUser().getCompany().getTitle());
        map.put("summaryNumbers", dashboardService.getSummaryNumbers());
        map.put("invoices", invoiceService.findLastThreeInvoices());
        map.put("exchangeRates", dashboardService.getExchangeRates());



        return ResponseEntity.ok(new ResponseWrapper("Dashboard successfully retrieved",map, HttpStatus.OK));

    }
}