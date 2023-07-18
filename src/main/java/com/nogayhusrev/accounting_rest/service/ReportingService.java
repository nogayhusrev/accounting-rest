package com.nogayhusrev.accounting_rest.service;

import com.nogayhusrev.accounting_rest.dto.InvoiceProductDto;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReportingService {

    List<InvoiceProductDto> getStock() throws AccountingProjectException;

    Map<String, BigDecimal> getProfitLoss();

}
