package com.couriercompany.courier_company_api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlreadyExistsException extends RuntimeException{

    private String debugMessage;

    public AlreadyExistsException(String message) {

        super(message);
    }

    public AlreadyExistsException(String message, String debugMessage) {
        super(message);
        this.debugMessage = debugMessage;
    }



}

