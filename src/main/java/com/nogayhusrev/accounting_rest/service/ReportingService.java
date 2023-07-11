package com.nogayhusrev.accounting_rest.service;

import com.nogayhusrev.accounting_rest.dto.InvoiceProductDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReportingService {

    List<InvoiceProductDto> getStock();

    Map<String, BigDecimal> getProfitLoss();

}
