package com.couriercompany.courier_company_api.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    private String debugMessage;

    public InvalidCredentialsException(String message) {

        super(message);
    }

    public InvalidCredentialsException(String message, String debugMessage) {
        super(message);
        this.debugMessage = debugMessage;
    }

}
