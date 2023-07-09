package com.nogayhusrev.accountingrest.controller;


import com.nogayhusrev.accountingrest.dto.ResponseWrapper;
import com.nogayhusrev.accountingrest.service.CompanyService;
import com.nogayhusrev.accountingrest.service.DashboardService;
import com.nogayhusrev.accountingrest.service.InvoiceService;
import com.nogayhusrev.accountingrest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
