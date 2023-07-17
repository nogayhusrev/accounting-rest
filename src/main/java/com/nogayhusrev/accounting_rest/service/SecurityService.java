package com.nogayhusrev.accounting_rest.service;

import com.nogayhusrev.accounting_rest.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.ws.rs.core.Response;

public interface SecurityService extends UserDetailsService {

    UserDto getCurrentUser();

    Response userCreate(UserDto userDto);

    void delete(String username);

}
