package com.couriercompany.courier_company_api.exceptions;

public class UnauthorizedException  extends RuntimeException{
    private String debugMessage;

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, String debugMessage) {
        super(message);
        this.debugMessage = debugMessage;
    }
}
