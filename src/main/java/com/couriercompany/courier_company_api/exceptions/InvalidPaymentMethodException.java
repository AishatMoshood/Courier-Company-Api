package com.couriercompany.courier_company_api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidPaymentMethodException extends RuntimeException{

    private String debugMessage;

    public InvalidPaymentMethodException(String message) {

        super(message);
    }

    public InvalidPaymentMethodException(String message, String debugMessage) {
        super(message);
        this.debugMessage = debugMessage;
    }



}

