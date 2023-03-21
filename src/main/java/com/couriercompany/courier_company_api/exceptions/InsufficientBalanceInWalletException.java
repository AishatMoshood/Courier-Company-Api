package com.couriercompany.courier_company_api.exceptions;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsufficientBalanceInWalletException extends RuntimeException{
    private String debugMessage;

    public InsufficientBalanceInWalletException(String message) {

        super(message);
    }

    public InsufficientBalanceInWalletException(String message, String debugMessage) {
        super(message);
        this.debugMessage = debugMessage;
    }
}
