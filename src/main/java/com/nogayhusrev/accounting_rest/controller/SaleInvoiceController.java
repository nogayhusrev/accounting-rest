package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.dto.InvoiceDto;
import com.nogayhusrev.accounting_rest.dto.InvoiceProductDto;
import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.enums.InvoiceType;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.service.ClientVendorService;
import com.nogayhusrev.accounting_rest.service.InvoiceProductService;
import com.nogayhusrev.accounting_rest.service.InvoiceService;
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
@RequestMapping("/api/v1/salesInvoices")
@Tag(name = "Sale Invoice Controller", description = "Sale Invoice API")
public class SaleInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;


    public SaleInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, ProductService productService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
    }


    @GetMapping()
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Read all Sale Invoices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Sale Invoices (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> list() throws Exception {

        List<InvoiceDto> saleInvoices = invoiceService.findSaleInvoices();
        return ResponseEntity.ok(new ResponseWrapper("Sale Invoices are successfully retrieved", saleInvoices, HttpStatus.OK));
    }

    @GetMapping("/{saleInvoiceId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Read one Sale Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Sale Invoice (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> getInvoiceById(@PathVariable Long saleInvoiceId) throws AccountingProjectException {

        InvoiceDto saleInvoice = invoiceService.findById(saleInvoiceId);

        return ResponseEntity.ok(new ResponseWrapper("Sale Invoice successfully retrieved", saleInvoice, HttpStatus.OK));
    }


    @PostMapping()
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Create a Sale Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created Sale Invoice (CREATED)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> create(@RequestBody InvoiceDto invoiceDto) throws Exception {

        if (invoiceService.isExist(invoiceDto)) {
            throw new Exception("This Invoice No already exists");
        }

        invoiceDto.setInvoiceNo(invoiceService.generateInvoiceNo(InvoiceType.SALES));
        invoiceService.save(invoiceDto, InvoiceType.SALES);
        InvoiceDto savedSaleInvoice = invoiceService.findByName(invoiceDto.getInvoiceNo());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Sale Invoice successfully created", savedSaleInvoice, HttpStatus.CREATED));
    }

    @PutMapping("/{saleInvoiceId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Update a Sale Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated Sale Invoice (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> update(@RequestBody InvoiceDto invoiceDto, @PathVariable Long saleInvoiceId) throws Exception {


        if (invoiceService.isExist(invoiceDto, saleInvoiceId)) {
            throw new Exception("This Purchase Invoice no already exists");
        }

        invoiceService.update(invoiceDto, saleInvoiceId);
        InvoiceDto updatedSaleInvoice = invoiceService.findById(saleInvoiceId);

        return ResponseEntity.ok(new ResponseWrapper("Sale Invoice successfully updated", updatedSaleInvoice, HttpStatus.OK));
    }

    @DeleteMapping("/{saleInvoiceId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Delete an Sale Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted Sale Invoice (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long saleInvoiceId) throws AccountingProjectException {

        invoiceService.delete(saleInvoiceId);

        return ResponseEntity.ok(new ResponseWrapper("Sale Invoice successfully deleted", HttpStatus.OK));
    }

    @GetMapping("/approve/{saleInvoiceId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Approve one Sale Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully approved Sale Invoice (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> approve(@PathVariable Long saleInvoiceId) throws AccountingProjectException {

        invoiceService.approve(saleInvoiceId);

        InvoiceDto invoiceDto = invoiceService.findById(saleInvoiceId);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("Sale Invoice approved", invoiceDto, HttpStatus.OK));
    }

    @PostMapping("/addInvoiceProduct/{saleInvoiceId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Add one product to Sale Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added one product to Sale Invoice (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> addInvoiceProductToSaleInvoice(@RequestBody InvoiceProductDto invoiceProductDto, @PathVariable Long saleInvoiceId) throws AccountingProjectException {


        invoiceProductService.saveInvoiceProductByInvoiceId(invoiceProductDto, saleInvoiceId);

        InvoiceDto invoiceDto = invoiceService.findById(saleInvoiceId);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("Sale Invoice Products added", invoiceDto, HttpStatus.OK));
    }


}
