package com.nogayhusrev.accounting_rest.dto;

import com.nogayhusrev.accounting_rest.enums.Months;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentDto {
    private Long id;
    private CompanyDto company;
    private Months month;
    private int amount;
    private boolean isPaid;
    private LocalDate year;
}
