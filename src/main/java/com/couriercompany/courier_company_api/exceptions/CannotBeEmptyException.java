package com.couriercompany.courier_company_api.exceptions;

public class CannotBeEmptyException extends RuntimeException{
    private String debugMessage;

    public CannotBeEmptyException(String message) {

        super(message);
    }

    public CannotBeEmptyException(String message, String debugMessage) {
        super(message);
        this.debugMessage = debugMessage;
    }

}
