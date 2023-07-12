package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.dto.InvoiceProductDto;
import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.service.ReportingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "REPORT API", description = "Reporting Operations")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }


    @GetMapping("/stock")
    public ResponseEntity<ResponseWrapper> getStock() throws Exception {

        List<InvoiceProductDto> stock = reportingService.getStock();
        return ResponseEntity.ok(new ResponseWrapper("Stock successfully retrieved", stock, HttpStatus.OK));
    }

    @GetMapping("/profitLoss")
    public ResponseEntity<ResponseWrapper> getProfitLoss() throws Exception {

        Map<String, BigDecimal> profitLoss = reportingService.getProfitLoss();

        return ResponseEntity.ok(new ResponseWrapper("Profit/Loss successfully retrieved", profitLoss, HttpStatus.OK));
    }


    @GetMapping("/logs")
    public String getLogs(Model model) {


        return "/report/logs";
    }


}
