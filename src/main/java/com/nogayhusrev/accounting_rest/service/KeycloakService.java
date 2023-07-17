package com.nogayhusrev.accounting_rest.service;


import com.nogayhusrev.accounting_rest.dto.UserDto;

import javax.ws.rs.core.Response;

public interface KeycloakService {

    Response userCreate(UserDto dto);

    void  delete(String username);
}
