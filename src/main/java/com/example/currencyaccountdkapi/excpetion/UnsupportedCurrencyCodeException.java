package com.example.currencyaccountdkapi.excpetion;

public class UnsupportedCurrencyCodeException extends RuntimeException {
    public UnsupportedCurrencyCodeException(String message) {
        super(message);
    }
}
