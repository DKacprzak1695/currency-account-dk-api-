package com.example.currencyaccountdkapi.excpetion;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
