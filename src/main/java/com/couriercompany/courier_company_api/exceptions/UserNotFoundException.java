package com.couriercompany.courier_company_api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNotFoundException extends RuntimeException{
    private String debugMessage;

    public UserNotFoundException(String message) {

        super(message);
    }

    public UserNotFoundException(String message, String debugMessage) {

        super(message);
        this.debugMessage = debugMessage;
    }
}
