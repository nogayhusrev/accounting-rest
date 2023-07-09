package com.nogayhusrev.accountingrest.repository;

import com.nogayhusrev.accountingrest.entity.InvoiceProduct;
import com.nogayhusrev.accountingrest.entity.Product;
import com.nogayhusrev.accountingrest.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

    List<InvoiceProduct> findInvoiceProductsByInvoiceId(Long invoiceId);


    List<InvoiceProduct> findAllByInvoice_Id(Long invoiceId);

    List<InvoiceProduct> findInvoiceProductsByInvoiceInvoiceTypeAndProductAndRemainingQuantityNotOrderByIdAsc(InvoiceType type, Product product, Integer remainingQuantity);

    List<InvoiceProduct> findAllInvoiceProductByProductId(Long id);
}
