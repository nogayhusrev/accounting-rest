package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.dto.UserDto;
import com.nogayhusrev.accounting_rest.service.CompanyService;
import com.nogayhusrev.accounting_rest.service.RoleService;
import com.nogayhusrev.accounting_rest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "USER API", description = "User CRUD Operations")
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    private final CompanyService companyService;

    public UserController(UserService userService, RoleService roleService, CompanyService companyService) {
        this.userService = userService;
        this.roleService = roleService;
        this.companyService = companyService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Read all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Users (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> list() throws Exception {
        List<UserDto> userDtoList = userService.findAll();
        return ResponseEntity.ok(new ResponseWrapper("Users are successfully retrieved", userDtoList, HttpStatus.OK));
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Read one user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved User (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> list(@PathVariable Long userId) throws Exception {
        UserDto userDto = userService.findById(userId);
        return ResponseEntity.ok(new ResponseWrapper("User successfully retrieved", userDto, HttpStatus.OK));
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created User (CREATED)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> create(@RequestBody UserDto userDto) throws Exception {

        if (userService.isExist(userDto)) {
            throw new Exception("This username already exists");
        }

        userService.save(userDto);
        UserDto savedUser = userService.findByName(userDto.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User successfully created", savedUser, HttpStatus.CREATED));
    }


    @PutMapping(value = "/{userId}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated User (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted User (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long userId) throws Exception {

        userService.delete(userId);

        return ResponseEntity.ok(new ResponseWrapper("User successfully deleted", HttpStatus.OK));
    }


}
