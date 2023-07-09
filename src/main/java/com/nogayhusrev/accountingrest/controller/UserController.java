package com.nogayhusrev.accountingrest.controller;


import com.nogayhusrev.accountingrest.dto.ResponseWrapper;
import com.nogayhusrev.accountingrest.dto.UserDto;
import com.nogayhusrev.accountingrest.service.CompanyService;
import com.nogayhusrev.accountingrest.service.RoleService;
import com.nogayhusrev.accountingrest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    private final CompanyService companyService;

    public UserController(UserService userService, RoleService roleService, CompanyService companyService) {
        this.userService = userService;
        this.roleService = roleService;
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> list() throws Exception {
        List<UserDto> userDtoList = userService.findAll();
        return ResponseEntity.ok(new ResponseWrapper("Users are successfully retrieved", userDtoList, HttpStatus.OK));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseWrapper> list(@PathVariable Long userId) throws Exception {
        UserDto userDto = userService.findById(userId);
        return ResponseEntity.ok(new ResponseWrapper("User successfully retrieved", userDto, HttpStatus.OK));
    }


    @PostMapping
    public ResponseEntity<ResponseWrapper> create(@RequestBody UserDto userDto) throws Exception {

        if (userService.isExist(userDto)) {
            throw new Exception("This username already exists");
        }

        userService.save(userDto);
        UserDto savedUser = userService.findByName(userDto.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User successfully created", savedUser, HttpStatus.CREATED));
    }


    @PutMapping("/{userId}")
    public ResponseEntity<ResponseWrapper> update(@RequestBody UserDto userDto, @PathVariable Long userId) throws Exception {

        if (userService.isExist(userDto, userId)) {
            throw new Exception("This Product description already exists");
        }

        userService.update(userDto, userId);
        UserDto updatedUser = userService.findById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User successfully updated", updatedUser, HttpStatus.OK));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long userId) throws Exception {

        userService.delete(userId);

        return ResponseEntity.ok(new ResponseWrapper("User successfully deleted", HttpStatus.OK));
    }


}
