package com.example.currencyaccountdkapi.service;

import com.example.currencyaccountdkapi.model.request.ExchangeRequest;
import com.example.currencyaccountdkapi.model.request.CreateUserAccountRequest;
import com.example.currencyaccountdkapi.model.response.UserAccountResponse;

public interface UserAccountService {

    UserAccountResponse registerUser(CreateUserAccountRequest command);

    UserAccountResponse getUserAccountByPesel(String pesel);

    UserAccountResponse exchangeCurrency(String pesel, ExchangeRequest request);
}
