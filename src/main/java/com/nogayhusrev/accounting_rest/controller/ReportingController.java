package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.dto.InvoiceProductDto;
import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.service.ReportingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Reporting Controller", description = "Reporting API")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }


    @GetMapping("/stock")
    @RolesAllowed("Manager")
    @Operation(summary = "Get Stock Data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock successfully retrieved (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> getStock() {

        List<InvoiceProductDto> stock = reportingService.getStock();
        return ResponseEntity.ok(new ResponseWrapper("Stock successfully retrieved", stock, HttpStatus.OK));
    }

    @GetMapping("/profitLoss")
    @RolesAllowed("Manager")
    @Operation(summary = "Calculate and Read Profit/Loss")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profit/Loss successfully retrieved (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> getProfitLoss() {

        Map<String, BigDecimal> profitLoss = reportingService.getProfitLoss();

        return ResponseEntity.ok(new ResponseWrapper("Profit/Loss successfully retrieved", profitLoss, HttpStatus.OK));
    }


}
