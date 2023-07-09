package com.nogayhusrev.accountingrest.service;

import com.nogayhusrev.accountingrest.dto.RoleDto;
import com.nogayhusrev.accountingrest.service.common.CrudService;

import java.util.List;

public interface RoleService extends CrudService<RoleDto, Long> {

    List<RoleDto> getRolesForCurrentUser();
}
