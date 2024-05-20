package com.example.currencyaccountdkapi.excpetion;

public class NoExchangeRateDataException extends RuntimeException {
    public NoExchangeRateDataException(String message) {
        super(message);
    }

    public NoExchangeRateDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
