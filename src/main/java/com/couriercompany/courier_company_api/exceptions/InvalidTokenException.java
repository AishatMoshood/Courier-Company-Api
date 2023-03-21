package com.couriercompany.courier_company_api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidTokenException extends RuntimeException{
    private String debugMessage;

    public InvalidTokenException(String message) {

        super(message);
    }

    public InvalidTokenException(String message, String debugMessage) {
        super(message);
        this.debugMessage = debugMessage;
    }
}
