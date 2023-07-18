package com.nogayhusrev.accounting_rest.service;

import com.nogayhusrev.accounting_rest.dto.CompanyDto;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.service.common.CrudService;

import java.util.List;

public interface CompanyService extends CrudService<CompanyDto, Long> {
    void activate(Long companyId) throws AccountingProjectException;

    void deactivate(Long companyId) throws AccountingProjectException;


}
