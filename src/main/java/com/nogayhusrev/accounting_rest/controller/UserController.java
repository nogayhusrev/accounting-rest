package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.dto.UserDto;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.service.CompanyService;
import com.nogayhusrev.accounting_rest.service.RoleService;
import com.nogayhusrev.accounting_rest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller", description = "User API")
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    private final CompanyService companyService;

    public UserController(UserService userService, RoleService roleService, CompanyService companyService) {
        this.userService = userService;
        this.roleService = roleService;
        this.companyService = companyService;
    }

    @GetMapping()
    @RolesAllowed({"Root", "Admin"})
    @Operation(summary = "Read all Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Users (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> list() throws AccountingProjectException {
        List<UserDto> userDtoList = userService.findAll();
        return ResponseEntity.ok(new ResponseWrapper("Users are successfully retrieved", userDtoList, HttpStatus.OK));
    }

    @GetMapping("/{userId}")
    @RolesAllowed({"Root", "Admin"})
    @Operation(summary = "Read one user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved User (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> list(@PathVariable Long userId) throws AccountingProjectException {
        UserDto userDto = userService.findById(userId);
        return ResponseEntity.ok(new ResponseWrapper("User successfully retrieved", userDto, HttpStatus.OK));
    }


    @PostMapping()
    @RolesAllowed({"Root", "Admin"})
    @Operation(summary = "Create an User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created User (CREATED)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> create(@RequestBody UserDto userDto) throws AccountingProjectException {

        if (userService.isExist(userDto)) {
            throw new AccountingProjectException("This username already exists");
        }

        userService.save(userDto);
        UserDto savedUser = userService.findByName(userDto.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User successfully created", savedUser, HttpStatus.CREATED));
    }


    @PutMapping("/{userId}")
    @RolesAllowed({"Root", "Admin"})
    @Operation(summary = "Update an User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated User (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> update(@RequestBody UserDto userDto, @PathVariable Long userId) throws AccountingProjectException {

        if (userService.isExist(userDto, userId)) {
            throw new AccountingProjectException("This Product description already exists");
        }

        userService.update(userDto, userId);
        UserDto updatedUser = userService.findById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User successfully updated", updatedUser, HttpStatus.OK));
    }

    @DeleteMapping("/{userId}")
    @RolesAllowed({"Root", "Admin"})
    @Operation(summary = "Delete an User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted User (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long userId) throws AccountingProjectException {

        userService.delete(userId);

        return ResponseEntity.ok(new ResponseWrapper("User successfully deleted", HttpStatus.OK));
    }


}
