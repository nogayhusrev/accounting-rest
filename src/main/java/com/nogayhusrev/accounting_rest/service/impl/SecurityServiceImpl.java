package com.nogayhusrev.accounting_rest.service.impl;

import com.nogayhusrev.accounting_rest.dto.UserDto;
import com.nogayhusrev.accounting_rest.repository.UserRepository;
import com.nogayhusrev.accounting_rest.service.SecurityService;
import com.nogayhusrev.accounting_rest.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;

    private final UserService userService;

    @Value("${ROOT_USERNAME}")
    private String rootUsername;

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    @Value("${MANAGER_USERNAME}")
    private String managerUsername;

    @Value("${EMPLOYEE_USERNAME}")
    private String employeeUsername;

    public SecurityServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @Override
    public UserDto getCurrentUser() {


        return userService.findByUsername(managerUsername);
    }
}
