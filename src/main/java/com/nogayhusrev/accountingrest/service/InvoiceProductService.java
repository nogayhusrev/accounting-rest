package com.nogayhusrev.accountingrest.service;

import com.nogayhusrev.accountingrest.dto.InvoiceProductDto;
import com.nogayhusrev.accountingrest.entity.InvoiceProduct;
import com.nogayhusrev.accountingrest.entity.Product;
import com.nogayhusrev.accountingrest.enums.InvoiceType;
import com.nogayhusrev.accountingrest.service.common.CrudService;

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
