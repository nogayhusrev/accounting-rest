package com.nogayhusrev.accounting_rest.enums;

import lombok.Getter;

@Getter
public enum InvoiceStatus {
    AWAITING_APPROVAL("Awaiting Approval"),
    APPROVED("Approved");

    private final String value;

    InvoiceStatus(String value) {
        this.value = value;
    }
}