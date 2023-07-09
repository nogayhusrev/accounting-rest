package com.nogayhusrev.accountingrest.service;

import com.nogayhusrev.accountingrest.dto.ClientVendorDto;
import com.nogayhusrev.accountingrest.dto.InvoiceDto;
import com.nogayhusrev.accountingrest.enums.InvoiceStatus;
import com.nogayhusrev.accountingrest.enums.InvoiceType;
import com.nogayhusrev.accountingrest.service.common.CrudService;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService extends CrudService<InvoiceDto, Long> {

    List<InvoiceDto> findPurchaseInvoices();

    List<InvoiceDto> findSaleInvoices();

    List<ClientVendorDto> findVendors();

    List<ClientVendorDto> findClients();

    String generateInvoiceNo(InvoiceType invoiceType);

    void save(InvoiceDto invoiceDto, InvoiceType invoiceType);

    void approve(Long invoiceId);

    List<InvoiceDto> findLastThreeInvoices();

    List<InvoiceDto> findInvoiceByInvoiceStatus(InvoiceStatus invoiceStatus);

    void printInvoice(Long invoiceId);

    BigDecimal getTotalPriceOfInvoice(Long invoiceId);

    BigDecimal getTotalTaxOfInvoice(Long invoiceId);

    BigDecimal getProfitLossOfInvoice(Long invoiceId);
}
