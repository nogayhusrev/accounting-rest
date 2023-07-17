package com.nogayhusrev.accounting_rest.exception;

public class CategoryNotFoundException extends AccountingProjectException{
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
