package com.nogayhusrev.accounting_rest.service;

import com.nogayhusrev.accounting_rest.dto.InvoiceProductDto;
import com.nogayhusrev.accounting_rest.entity.InvoiceProduct;
import com.nogayhusrev.accounting_rest.entity.Product;
import com.nogayhusrev.accounting_rest.enums.InvoiceType;
import com.nogayhusrev.accounting_rest.service.common.CrudService;

import java.util.List;

public interface InvoiceProductService extends CrudService<InvoiceProductDto, Long> {
    List<InvoiceProductDto> findInvoiceProductsByInvoiceId(Long invoiceId);

    List<InvoiceProduct> findInvoiceProductsByInvoiceType(InvoiceType invoiceType);

    void saveInvoiceProductByInvoiceId(InvoiceProductDto invoiceProductDto, Long invoiceId);

    void completeApprovalProcedures(Long invoiceId, InvoiceType type);

    boolean checkProductQuantity(InvoiceProductDto salesInvoiceProduct);

    List<InvoiceProduct> findInvoiceProductsByInvoiceTypeAndProductRemainingQuantity(InvoiceType type, Product product, Integer remainingQuantity);

    List<InvoiceProduct> findAllInvoiceProductsByProductId(Long id);

}
