package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.dto.CategoryDto;
import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Category Controller", description = "Category API")
public class CategoryController {

    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Read all Categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Categories (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> list() throws AccountingProjectException {
        List<CategoryDto> categoryList = categoryService.findAll();
        return ResponseEntity.ok(new ResponseWrapper("Categories are successfully retrieved", categoryList, HttpStatus.OK));
    }

    @GetMapping("/{categoryId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Read one Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Category (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> list(@PathVariable Long categoryId) throws AccountingProjectException {
        CategoryDto categoryDto = categoryService.findById(categoryId);
        return ResponseEntity.ok(new ResponseWrapper("Category successfully retrieved", categoryDto, HttpStatus.OK));
    }

    @PostMapping
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Create a Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created Category (CREATED)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> create(@RequestBody CategoryDto categoryDto) throws AccountingProjectException {

        if (categoryService.isExist(categoryDto)) {
            throw new AccountingProjectException("This category description already exists");
        }

        categoryService.save(categoryDto);
        CategoryDto savedCategory = categoryService.findByName(categoryDto.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Category successfully created", savedCategory, HttpStatus.CREATED));
    }

    @PutMapping("/{categoryId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Update a Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated Category (CREATED)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> update(@RequestBody CategoryDto categoryDto, @PathVariable Long categoryId) throws AccountingProjectException {


        if (categoryService.isExist(categoryDto, categoryId)) {
            throw new AccountingProjectException("This category description already exists");
        }

        categoryService.update(categoryDto, categoryId);
        CategoryDto updatedCategory = categoryService.findByName(categoryDto.getDescription());

        return ResponseEntity.ok(new ResponseWrapper("Category successfully updated", updatedCategory, HttpStatus.OK));
    }

    @DeleteMapping("/{categoryId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Delete a Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted Category (CREATED)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long categoryId) throws AccountingProjectException {

        if (categoryService.findById(categoryId).isHasProduct()) {
            throw new AccountingProjectException("This category has products.");
        }

        categoryService.delete(categoryId);
        return ResponseEntity.ok(new ResponseWrapper("Category successfully deleted", HttpStatus.OK));
    }


}
