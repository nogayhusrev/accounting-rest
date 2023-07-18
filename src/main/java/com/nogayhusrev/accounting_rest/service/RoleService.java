package com.nogayhusrev.accounting_rest.service;

import com.nogayhusrev.accounting_rest.dto.RoleDto;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.service.common.CrudService;

import java.util.List;

public interface RoleService extends CrudService<RoleDto, Long> {

    List<RoleDto> getRolesForCurrentUser() throws AccountingProjectException;
}
