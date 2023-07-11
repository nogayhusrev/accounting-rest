package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.dto.InvoiceDto;
import com.nogayhusrev.accounting_rest.dto.InvoiceProductDto;
import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.enums.InvoiceType;
import com.nogayhusrev.accounting_rest.service.ClientVendorService;
import com.nogayhusrev.accounting_rest.service.InvoiceProductService;
import com.nogayhusrev.accounting_rest.service.InvoiceService;
import com.nogayhusrev.accounting_rest.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salesInvoices")
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

    @GetMapping
    public ResponseEntity<ResponseWrapper> list() throws Exception {

        List<InvoiceDto> saleInvoices = invoiceService.findSaleInvoices();
        return ResponseEntity.ok(new ResponseWrapper("Sale Invoices are successfully retrieved", saleInvoices, HttpStatus.OK));
    }

    @GetMapping("/{saleInvoiceId}")
    public ResponseEntity<ResponseWrapper> getInvoiceById(@PathVariable Long saleInvoiceId) {

        InvoiceDto saleInvoice = invoiceService.findById(saleInvoiceId);

        return ResponseEntity.ok(new ResponseWrapper("Sale Invoice successfully retrieved", saleInvoice, HttpStatus.OK));
    }

    @PostMapping
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
    public ResponseEntity<ResponseWrapper> update(@RequestBody InvoiceDto invoiceDto, @PathVariable Long saleInvoiceId) throws Exception {


        if (invoiceService.isExist(invoiceDto, saleInvoiceId)) {
            throw new Exception("This Purchase Invoice no already exists");
        }

        invoiceService.update(invoiceDto, saleInvoiceId);
        InvoiceDto updatedSaleInvoice = invoiceService.findById(saleInvoiceId);

        return ResponseEntity.ok(new ResponseWrapper("Sale Invoice successfully updated", updatedSaleInvoice, HttpStatus.OK));
    }

    @DeleteMapping("/{saleInvoiceId}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long saleInvoiceId) {

        invoiceService.delete(saleInvoiceId);

        return ResponseEntity.ok(new ResponseWrapper("Sale Invoice successfully deleted", HttpStatus.OK));
    }

    @GetMapping("/approve/{saleInvoiceId}")
    public ResponseEntity<ResponseWrapper> approve(@PathVariable Long saleInvoiceId) {

        invoiceService.approve(saleInvoiceId);

        InvoiceDto invoiceDto = invoiceService.findById(saleInvoiceId);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("Sale Invoice approved", invoiceDto, HttpStatus.OK));
    }

    @PostMapping("/addInvoiceProduct/{saleInvoiceId}")
    public ResponseEntity<ResponseWrapper> addInvoiceProductToSaleInvoice(@RequestBody InvoiceProductDto invoiceProductDto, @PathVariable Long saleInvoiceId) {


        invoiceProductService.saveInvoiceProductByInvoiceId(invoiceProductDto, saleInvoiceId);

        InvoiceDto invoiceDto = invoiceService.findById(saleInvoiceId);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("Sale Invoice Products added", invoiceDto, HttpStatus.OK));
    }



}
