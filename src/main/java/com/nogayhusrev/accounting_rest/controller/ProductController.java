package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.dto.ProductDto;
import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.service.ProductService;
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
@RequestMapping("/api/v1/products")
@Tag(name = "Product Controller", description = "Product API")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping()
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Read all Products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Products (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> list() throws AccountingProjectException {
        List<ProductDto> productDtoList = productService.findAll();
        return ResponseEntity.ok(new ResponseWrapper("Products are successfully retrieved", productDtoList, HttpStatus.OK));
    }

    @GetMapping("/{productId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Read one Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Product (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> list(@PathVariable Long productId) throws AccountingProjectException {
        ProductDto productDto = productService.findById(productId);
        return ResponseEntity.ok(new ResponseWrapper("Product successfully retrieved", productDto, HttpStatus.OK));
    }


    @PostMapping()
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Create a Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created Product (CREATED)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> create(@RequestBody ProductDto productDto) throws AccountingProjectException {

        if (productService.isExist(productDto)) {
            throw new AccountingProjectException("This Product description already exists");
        }

        productService.save(productDto);
        ProductDto savedProduct = productService.findByName(productDto.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Product successfully created", savedProduct, HttpStatus.CREATED));
    }


    @PutMapping("/{productId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Update a Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated Product (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> update(@RequestBody ProductDto productDto, @PathVariable Long productId) throws AccountingProjectException {

        if (productService.isExist(productDto, productId)) {
            throw new AccountingProjectException("This Product description already exists");
        }

        productService.update(productDto, productId);
        ProductDto updatedProduct = productService.findById(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Product successfully updated", updatedProduct, HttpStatus.OK));
    }

    @DeleteMapping("/{productId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Delete a Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted Product (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long productId) throws AccountingProjectException {

        if (productService.findById(productId).getQuantityInStock() > 0) {
            throw new AccountingProjectException("This Products has quantity in stock.");
        }

        productService.delete(productId);
        return ResponseEntity.ok(new ResponseWrapper("Product successfully deleted", HttpStatus.OK));
    }


}
