package com.nogayhusrev.accounting_rest.service.impl;

import com.nogayhusrev.accounting_rest.dto.RoleDto;
import com.nogayhusrev.accounting_rest.entity.Role;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.mapper.MapperUtil;
import com.nogayhusrev.accounting_rest.repository.RoleRepository;
import com.nogayhusrev.accounting_rest.service.RoleService;
import com.nogayhusrev.accounting_rest.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MapperUtil mapperUtil;
    private final UserService userService;

    public RoleServiceImpl(RoleRepository roleRepository, MapperUtil mapperUtil, UserService userService) {
        this.roleRepository = roleRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
    }


    @Override
    public RoleDto findById(Long roleId) throws AccountingProjectException {
        throw new AccountingProjectException("FIND_BY_ID METHOD NOT IMPLEMENTED");
    }

    @Override
    public List<RoleDto> getRolesForCurrentUser() throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");


        List<Role> roles;

        if (userService.getCurrentUser().getRole().getDescription().equals("Root User")) {
            roles = roleRepository.findAll().stream()
                    .filter(role -> role.getDescription().equals("Admin")).collect(Collectors.toList());
        } else {
            roles = roleRepository.findAll().stream()
                    .filter(role -> !role.getDescription().equals("Root User"))
                    .collect(Collectors.toList());
        }

        return roles.stream().map(role -> mapperUtil.convert(role, new RoleDto())).collect(Collectors.toList());
    }


    @Override
    public List<RoleDto> findAll() throws AccountingProjectException {
        throw new AccountingProjectException("FIND_ALL METHOD NOT IMPLEMENTED");
    }

    @Override
    public RoleDto findByName(String name) throws AccountingProjectException {
        throw new AccountingProjectException("FIND_BY_NAME METHOD NOT IMPLEMENTED");
    }

    @Override
    public void save(RoleDto roleDto) throws AccountingProjectException {
        throw new AccountingProjectException("SAVE METHOD NOT IMPLEMENTED");

    }

    @Override
    public void delete(Long roleId) throws AccountingProjectException {
        throw new AccountingProjectException("DELETE METHOD NOT IMPLEMENTED");

    }

    @Override
    public void update(RoleDto roleDto, Long roleId) throws AccountingProjectException {
        throw new AccountingProjectException("UPDATE METHOD NOT IMPLEMENTED");

    }

    @Override
    public boolean isExist(RoleDto roleDto, Long roleId) throws AccountingProjectException {
        throw new AccountingProjectException("IS_EXIST METHOD NOT IMPLEMENTED");

    }

    @Override
    public boolean isExist(RoleDto roleDto) throws AccountingProjectException {
        throw new AccountingProjectException("IS_EXIST METHOD NOT IMPLEMENTED");
    }

    private boolean isNotAdminOrManager() {
        return !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Manager")
                &&
                !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Employee");
    }

}
