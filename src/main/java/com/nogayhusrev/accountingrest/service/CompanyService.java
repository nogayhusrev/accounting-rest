package com.nogayhusrev.accountingrest.service;

import com.nogayhusrev.accountingrest.dto.CompanyDto;
import com.nogayhusrev.accountingrest.service.common.CrudService;

import java.util.List;

public interface CompanyService extends CrudService<CompanyDto, Long> {
    void activate(Long companyId);

    void deactivate(Long companyId);

    List<CompanyDto> getCompaniesForCurrentUser();
}
