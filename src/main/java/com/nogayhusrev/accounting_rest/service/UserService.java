package com.nogayhusrev.accounting_rest.service;

import com.nogayhusrev.accounting_rest.dto.UserDto;
import com.nogayhusrev.accounting_rest.service.common.CrudService;

public interface UserService extends CrudService<UserDto, Long> {

    UserDto getCurrentUser();


}
