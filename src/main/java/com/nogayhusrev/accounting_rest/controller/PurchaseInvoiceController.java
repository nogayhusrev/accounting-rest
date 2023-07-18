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
@RequestMapping("/api/v1/purchaseInvoices")
@Tag(name = "Purchase Invoice Controller", description = "Purchase Invoice API")
public class PurchaseInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;


    public PurchaseInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, ProductService productService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
    }


    @GetMapping()
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Read all Purchase Invoices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Purchase Invoices (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> list() throws AccountingProjectException {

        List<InvoiceDto> purchaseInvoices = invoiceService.findPurchaseInvoices();
        return ResponseEntity.ok(new ResponseWrapper("Purchase Invoices are successfully retrieved", purchaseInvoices, HttpStatus.OK));
    }

    @GetMapping("/{purchaseInvoiceId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Read one Purchase Invoices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Purchase Invoice (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> getInvoiceById(@PathVariable Long purchaseInvoiceId) throws AccountingProjectException {

        InvoiceDto purchaseInvoice = invoiceService.findById(purchaseInvoiceId);

        return ResponseEntity.ok(new ResponseWrapper("Purchase Invoice successfully retrieved", purchaseInvoice, HttpStatus.OK));
    }

    @PostMapping()
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Create a Purchase Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created Purchase Invoice (CREATED)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> create(@RequestBody InvoiceDto invoiceDto) throws AccountingProjectException {

        if (invoiceService.isExist(invoiceDto)) {
            throw new AccountingProjectException("This Invoice No already exists");
        }

        invoiceDto.setInvoiceNo(invoiceService.generateInvoiceNo(InvoiceType.PURCHASE));
        invoiceService.save(invoiceDto, InvoiceType.PURCHASE);
        InvoiceDto savedPurchaseInvoice = invoiceService.findByName(invoiceDto.getInvoiceNo());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Purchase Invoice successfully created", savedPurchaseInvoice, HttpStatus.CREATED));
    }

    @PutMapping("/{purchaseInvoiceId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Update a Purchase Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated Purchase Invoice (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> update(@RequestBody InvoiceDto invoiceDto, @PathVariable Long purchaseInvoiceId) throws AccountingProjectException {


        if (invoiceService.isExist(invoiceDto, purchaseInvoiceId)) {
            throw new AccountingProjectException("This Purchase Invoice no already exists");
        }

        invoiceService.update(invoiceDto, purchaseInvoiceId);
        InvoiceDto updatedPurchaseInvoice = invoiceService.findById(purchaseInvoiceId);

        return ResponseEntity.ok(new ResponseWrapper("Purchase Invoice successfully updated", updatedPurchaseInvoice, HttpStatus.OK));
    }


    @DeleteMapping("/{purchaseInvoiceId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Delete an Purchase Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted Purchase Invoice (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long purchaseInvoiceId) throws AccountingProjectException {

        invoiceService.delete(purchaseInvoiceId);

        return ResponseEntity.ok(new ResponseWrapper("Purchase Invoice successfully deleted", HttpStatus.OK));
    }

    @GetMapping("/approve/{purchaseInvoiceId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Approve one Purchase Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully approved Purchase Invoice (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> approve(@PathVariable Long purchaseInvoiceId) throws AccountingProjectException {

        invoiceService.approve(purchaseInvoiceId);

        InvoiceDto invoiceDto = invoiceService.findById(purchaseInvoiceId);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("Purchase Invoice approved", invoiceDto, HttpStatus.OK));
    }


    @PostMapping("/addInvoiceProduct/{purchaseInvoiceId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Add one product to Purchase Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added one product to Purchase Invoice (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> addInvoiceProductToPurchaseInvoice(@RequestBody InvoiceProductDto invoiceProductDto, @PathVariable Long purchaseInvoiceId) throws AccountingProjectException {


        invoiceProductService.saveInvoiceProductByInvoiceId(invoiceProductDto, purchaseInvoiceId);

        InvoiceDto invoiceDto = invoiceService.findById(purchaseInvoiceId);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("Purchase Invoice Products added", invoiceDto, HttpStatus.OK));
    }


}
