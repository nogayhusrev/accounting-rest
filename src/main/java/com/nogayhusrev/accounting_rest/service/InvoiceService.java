package com.nogayhusrev.accounting_rest.service;

import com.nogayhusrev.accounting_rest.dto.ClientVendorDto;
import com.nogayhusrev.accounting_rest.dto.InvoiceDto;
import com.nogayhusrev.accounting_rest.enums.InvoiceStatus;
import com.nogayhusrev.accounting_rest.enums.InvoiceType;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.service.common.CrudService;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService extends CrudService<InvoiceDto, Long> {

    List<InvoiceDto> findPurchaseInvoices() throws AccountingProjectException;

    List<InvoiceDto> findSaleInvoices() throws AccountingProjectException;

    List<ClientVendorDto> findVendors() throws AccountingProjectException;

    List<ClientVendorDto> findClients() throws AccountingProjectException;

    String generateInvoiceNo(InvoiceType invoiceType) throws AccountingProjectException;

    void save(InvoiceDto invoiceDto, InvoiceType invoiceType) throws AccountingProjectException;

    void approve(Long invoiceId) throws AccountingProjectException;

    List<InvoiceDto> findLastThreeInvoices() throws AccountingProjectException;

    List<InvoiceDto> findInvoiceByInvoiceStatus(InvoiceStatus invoiceStatus) throws AccountingProjectException;

    void printInvoice(Long invoiceId) throws AccountingProjectException;

    BigDecimal getTotalPriceOfInvoice(Long invoiceId) throws AccountingProjectException;

    BigDecimal getTotalTaxOfInvoice(Long invoiceId) throws AccountingProjectException;

    BigDecimal getProfitLossOfInvoice(Long invoiceId) throws AccountingProjectException;
}
