package com.nogayhusrev.accounting_rest.entity;


import com.nogayhusrev.accounting_rest.enums.InvoiceStatus;
import com.nogayhusrev.accounting_rest.enums.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoices")
@Where(clause = "is_deleted=false")
public class Invoice extends BaseEntity {

    private String invoiceNo;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;


    private LocalDate date;


    @ManyToOne
    private ClientVendor clientVendor;


    @ManyToOne
    private Company company;
}
