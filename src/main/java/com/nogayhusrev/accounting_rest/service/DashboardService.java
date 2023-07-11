package com.nogayhusrev.accounting_rest.service;

import com.nogayhusrev.accounting_rest.dto.CurrencyDto;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService {

    Map<String, BigDecimal> getSummaryNumbers();

    CurrencyDto getExchangeRates();
}
