package com.couriercompany.courier_company_api.exceptions;

public class UnauthorizedUserException extends RuntimeException{
    private String debugMsg;
    public UnauthorizedUserException(String message) {
        super(message);
    }

    public UnauthorizedUserException(String message, String debugMsg) {
        super(message);
        this.debugMsg = debugMsg;
    }
}
