package com.nogayhusrev.accounting_rest.exception;

public class ProductNotFoundException extends AccountingProjectException{
    public ProductNotFoundException(String message) {
        super(message);
    }
}
