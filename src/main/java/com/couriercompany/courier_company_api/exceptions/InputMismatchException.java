package com.couriercompany.courier_company_api.exceptions;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputMismatchException extends RuntimeException{
    private String debugMessage;

    public InputMismatchException(String message) {

        super(message);
    }

    public InputMismatchException(String message, String debugMessage) {
        super(message);
        this.debugMessage = debugMessage;
    }
}
