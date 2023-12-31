package com.nogayhusrev.accounting_rest.enums;


import lombok.Getter;

@Getter
public enum ClientVendorType {
    CLIENT("Client"),
    VENDOR("Vendor");

    private final String value;

    ClientVendorType(String value) {
        this.value = value;
    }
}
