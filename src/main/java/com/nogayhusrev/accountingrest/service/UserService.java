package com.nogayhusrev.accountingrest.service;

import com.nogayhusrev.accountingrest.dto.UserDto;
import com.nogayhusrev.accountingrest.service.common.CrudService;

public interface UserService extends CrudService<UserDto, Long> {
    UserDto findByUsername(String name);

    UserDto getCurrentUser();

}
