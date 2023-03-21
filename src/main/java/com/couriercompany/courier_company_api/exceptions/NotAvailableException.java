package com.couriercompany.courier_company_api.exceptions;

public class NotAvailableException extends RuntimeException{
    private String debugMessage;

    public NotAvailableException(String message) {
        super(message);
    }

    public NotAvailableException(String message, String debugMessage) {
        super(message);
        this.debugMessage = debugMessage;
    }
}
