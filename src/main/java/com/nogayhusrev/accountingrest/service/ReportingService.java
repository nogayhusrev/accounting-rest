package com.nogayhusrev.accountingrest.service;

import com.nogayhusrev.accountingrest.dto.InvoiceProductDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReportingService {

    List<InvoiceProductDto> getStock();

    Map<String, BigDecimal> getProfitLoss();

}
