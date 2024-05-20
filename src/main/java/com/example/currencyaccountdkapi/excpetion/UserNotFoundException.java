package com.example.currencyaccountdkapi.excpetion;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
