package com.nogayhusrev.accountingrest.service;

import com.nogayhusrev.accountingrest.dto.CurrencyDto;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService {

    Map<String, BigDecimal> getSummaryNumbers();

    CurrencyDto getExchangeRates();
}
