package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.dto.UserDto;
import com.nogayhusrev.accounting_rest.service.CompanyService;
import com.nogayhusrev.accounting_rest.service.RoleService;
import com.nogayhusrev.accounting_rest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "User CRUD Operations")
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
    @Operation(summary = "Read all users")
    public ResponseEntity<ResponseWrapper> list() throws Exception {
        List<UserDto> userDtoList = userService.findAll();
        return ResponseEntity.ok(new ResponseWrapper("Users are successfully retrieved", userDtoList, HttpStatus.OK));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Read one user")
    public ResponseEntity<ResponseWrapper> list(@PathVariable Long userId) throws Exception {
        UserDto userDto = userService.findById(userId);
        return ResponseEntity.ok(new ResponseWrapper("User successfully retrieved", userDto, HttpStatus.OK));
    }


    @PostMapping
    @Operation(summary = "Create an user")
    public ResponseEntity<ResponseWrapper> create(@RequestBody UserDto userDto) throws Exception {

        if (userService.isExist(userDto)) {
            throw new Exception("This username already exists");
        }

        userService.save(userDto);
        UserDto savedUser = userService.findByName(userDto.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User successfully created", savedUser, HttpStatus.CREATED));
    }


    @PutMapping("/{userId}")
    @Operation(summary = "Update an user")
    public ResponseEntity<ResponseWrapper> update(@RequestBody UserDto userDto, @PathVariable Long userId) throws Exception {

        if (userService.isExist(userDto, userId)) {
            throw new Exception("This Product description already exists");
        }

        userService.update(userDto, userId);
        UserDto updatedUser = userService.findById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User successfully updated", updatedUser, HttpStatus.OK));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete an user")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long userId) throws Exception {

        userService.delete(userId);

        return ResponseEntity.ok(new ResponseWrapper("User successfully deleted", HttpStatus.OK));
    }


}
