package com.nogayhusrev.accountingrest.controller;


import com.nogayhusrev.accountingrest.dto.CategoryDto;
import com.nogayhusrev.accountingrest.dto.ProductDto;
import com.nogayhusrev.accountingrest.dto.ResponseWrapper;
import com.nogayhusrev.accountingrest.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<ResponseWrapper> list() throws Exception {
        List<ProductDto> productDtoList = productService.findAll();
        return ResponseEntity.ok(new ResponseWrapper("Products are successfully retrieved", productDtoList, HttpStatus.OK));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ResponseWrapper> list(@PathVariable Long productId) throws Exception {
        ProductDto productDto = productService.findById(productId);
        return ResponseEntity.ok(new ResponseWrapper("Product successfully retrieved", productDto, HttpStatus.OK));
    }


    @PostMapping
    public ResponseEntity<ResponseWrapper> create(@RequestBody ProductDto productDto) throws Exception {

        if (productService.isExist(productDto)) {
            throw new Exception("This Product description already exists");
        }

        productService.save(productDto);
        ProductDto savedProduct = productService.findByName(productDto.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Product successfully created", savedProduct, HttpStatus.CREATED));
    }


    @PutMapping("/{productId}")
    public ResponseEntity<ResponseWrapper> update(@RequestBody ProductDto productDto, @PathVariable Long productId) throws Exception {

        if (productService.isExist(productDto, productId)) {
            throw new Exception("This Product description already exists");
        }

        productService.update(productDto, productId);
        ProductDto updatedProduct = productService.findById(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Product successfully updated", updatedProduct, HttpStatus.OK));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long productId) throws Exception {

        if (productService.findById(productId).getQuantityInStock() > 0) {
            throw new Exception("This Products has quantity in stock.");
        }

        productService.delete(productId);
        return ResponseEntity.ok(new ResponseWrapper("Product successfully deleted", HttpStatus.OK));
    }


}
