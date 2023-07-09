package com.nogayhusrev.accountingrest.controller;


import com.nogayhusrev.accountingrest.dto.InvoiceDto;
import com.nogayhusrev.accountingrest.dto.InvoiceProductDto;
import com.nogayhusrev.accountingrest.dto.ResponseWrapper;
import com.nogayhusrev.accountingrest.enums.InvoiceType;
import com.nogayhusrev.accountingrest.service.ClientVendorService;
import com.nogayhusrev.accountingrest.service.InvoiceProductService;
import com.nogayhusrev.accountingrest.service.InvoiceService;
import com.nogayhusrev.accountingrest.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchaseInvoices")
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


    @GetMapping
    public ResponseEntity<ResponseWrapper> list() throws Exception {

        List<InvoiceDto> purchaseInvoices = invoiceService.findPurchaseInvoices();
        return ResponseEntity.ok(new ResponseWrapper("Purchase Invoices are successfully retrieved", purchaseInvoices, HttpStatus.OK));
    }

    @GetMapping("/{purchaseInvoiceId}")
    public ResponseEntity<ResponseWrapper> getInvoiceById(@PathVariable Long purchaseInvoiceId) {

        InvoiceDto invoice = invoiceService.findById(purchaseInvoiceId);

        return ResponseEntity.ok(new ResponseWrapper("Invoice successfully retrieved", invoice, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> create(@RequestBody InvoiceDto invoiceDto) throws Exception {

        if (invoiceService.isExist(invoiceDto)) {
            throw new Exception("This Invoice No already exists");
        }

        invoiceDto.setInvoiceNo(invoiceService.generateInvoiceNo(InvoiceType.PURCHASE));
        invoiceService.save(invoiceDto, InvoiceType.PURCHASE);
        InvoiceDto savedPurchaseInvoice = invoiceService.findByName(invoiceDto.getInvoiceNo());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Purchase Invoice successfully created", savedPurchaseInvoice, HttpStatus.CREATED));
    }

    @PutMapping("/{purchaseInvoiceId}")
    public ResponseEntity<ResponseWrapper> update(@RequestBody InvoiceDto invoiceDto, @PathVariable Long purchaseInvoiceId) throws Exception {


        if (invoiceService.isExist(invoiceDto, purchaseInvoiceId)) {
            throw new Exception("This Purchase Invoice no already exists");
        }

        invoiceService.update(invoiceDto, purchaseInvoiceId);
        InvoiceDto updatedPurchaseInvoice = invoiceService.findById(purchaseInvoiceId);

        return ResponseEntity.ok(new ResponseWrapper("Purchase Invoice successfully updated", updatedPurchaseInvoice, HttpStatus.OK));
    }


    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long invoiceId) {

        invoiceService.delete(invoiceId);

        return ResponseEntity.ok(new ResponseWrapper("Purchase Invoice successfully deleted", HttpStatus.OK));
    }

    @GetMapping("/approve/{invoiceId}")
    public ResponseEntity<ResponseWrapper> approve(@PathVariable Long invoiceId) {

        invoiceService.approve(invoiceId);

        InvoiceDto invoiceDto = invoiceService.findById(invoiceId);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("Invoice approved",invoiceDto, HttpStatus.OK));
    }


    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public ResponseEntity<ResponseWrapper> addInvoiceProductToPurchaseInvoice(@RequestBody InvoiceProductDto invoiceProductDto, @PathVariable Long invoiceId) {


        invoiceProductService.saveInvoiceProductByInvoiceId(invoiceProductDto, invoiceId);

        InvoiceDto invoiceDto = invoiceService.findById(invoiceId);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("Invoice Products added",invoiceDto, HttpStatus.OK));
    }


}
