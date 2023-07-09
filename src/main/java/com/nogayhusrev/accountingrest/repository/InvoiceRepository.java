package com.nogayhusrev.accountingrest.repository;

import com.nogayhusrev.accountingrest.entity.Company;
import com.nogayhusrev.accountingrest.entity.Invoice;
import com.nogayhusrev.accountingrest.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findInvoicesByCompanyAndInvoiceType(Company company, InvoiceType invoiceType);
}
