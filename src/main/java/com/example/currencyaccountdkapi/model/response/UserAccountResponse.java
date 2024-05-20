package com.example.currencyaccountdkapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountResponse {

    private String firstName;
    private String lastName;
    private String pesel;
    private BigDecimal plnBalance;
    private BigDecimal usdBalance;
}
