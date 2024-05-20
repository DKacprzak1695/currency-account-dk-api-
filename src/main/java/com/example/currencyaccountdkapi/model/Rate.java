package com.example.currencyaccountdkapi.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Rate {
    private String no;
    private String effectiveDate;
    private BigDecimal mid;
}
