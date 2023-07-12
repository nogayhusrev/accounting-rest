package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.dto.CategoryDto;
import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "CATEGORY API", description = "Category CRUD Operations")
public class CategoryController {

    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> list() throws Exception {
        List<CategoryDto> categoryList = categoryService.findAll();
        return ResponseEntity.ok(new ResponseWrapper("Categories are successfully retrieved", categoryList, HttpStatus.OK));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseWrapper> list(@PathVariable Long categoryId) throws Exception {
        CategoryDto categoryDto = categoryService.findById(categoryId);
        return ResponseEntity.ok(new ResponseWrapper("Category successfully retrieved", categoryDto, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> create(@RequestBody CategoryDto categoryDto) throws Exception {

        if (categoryService.isExist(categoryDto)) {
            throw new Exception("This category description already exists");
        }

        categoryService.save(categoryDto);
        CategoryDto savedCategory = categoryService.findByName(categoryDto.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Category successfully created", savedCategory, HttpStatus.CREATED));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ResponseWrapper> update(@RequestBody CategoryDto categoryDto, @PathVariable Long categoryId) throws Exception {


        if (categoryService.isExist(categoryDto, categoryId)) {
            throw new Exception("This category description already exists");
        }

        categoryService.update(categoryDto, categoryId);
        CategoryDto updatedCategory = categoryService.findByName(categoryDto.getDescription());

        return ResponseEntity.ok(new ResponseWrapper("Category successfully updated", updatedCategory, HttpStatus.OK));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long categoryId) throws Exception {

        if (categoryService.findById(categoryId).isHasProduct()) {
            throw new Exception("This category has products.");
        }

        categoryService.delete(categoryId);
        return ResponseEntity.ok(new ResponseWrapper("Category successfully deleted", HttpStatus.OK));
    }


}
