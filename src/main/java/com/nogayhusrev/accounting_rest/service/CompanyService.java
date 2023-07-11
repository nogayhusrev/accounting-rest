package com.nogayhusrev.accounting_rest.service;

import com.nogayhusrev.accounting_rest.dto.CompanyDto;
import com.nogayhusrev.accounting_rest.service.common.CrudService;

import java.util.List;

public interface CompanyService extends CrudService<CompanyDto, Long> {
    void activate(Long companyId);

    void deactivate(Long companyId);

    List<CompanyDto> getCompaniesForCurrentUser();
}
