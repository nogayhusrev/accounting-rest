package com.nogayhusrev.accountingrest.service.impl;

import com.nogayhusrev.accountingrest.dto.UserDto;
import com.nogayhusrev.accountingrest.repository.UserRepository;
import com.nogayhusrev.accountingrest.service.SecurityService;
import com.nogayhusrev.accountingrest.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;

    private final UserService userService;

    public SecurityServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @Override
    public UserDto getCurrentUser() {


        return userService.findByUsername("manager@greentech.com");
    }
}
