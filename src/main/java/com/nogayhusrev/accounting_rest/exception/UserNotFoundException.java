package com.nogayhusrev.accounting_rest.exception;

public class UserNotFoundException extends AccountingProjectException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
