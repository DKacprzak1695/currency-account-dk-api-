package com.example.currencyaccountdkapi.model.response;

import com.example.currencyaccountdkapi.model.Rate;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NbpResponse {
    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;
}
