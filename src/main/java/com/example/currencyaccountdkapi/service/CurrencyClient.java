package com.example.currencyaccountdkapi.service;

import com.example.currencyaccountdkapi.excpetion.NoExchangeRateDataException;
import com.example.currencyaccountdkapi.model.response.NbpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@Slf4j
public class CurrencyClient {

    private static final String NBP_API_URL = "http://api.nbp.pl/api/exchangerates/rates/A/USD?format=json";

    public BigDecimal getUsdExchangeRate() {
        RestTemplate restTemplate = new RestTemplate();
        log.info("Fetching USD exchange rate from NBP API: {}", NBP_API_URL);

        NbpResponse response = restTemplate.getForObject(NBP_API_URL, NbpResponse.class);

        if (response != null && response.getRates() != null && !response.getRates().isEmpty()) {
            BigDecimal exchangeRate = response.getRates().get(0).getMid();
            log.info("Successfully fetched USD exchange rate: {}", exchangeRate);
            return exchangeRate;
        } else {
            log.error("No exchange rates found in the response from NBP API");
            throw new NoExchangeRateDataException("No exchange rates found in the response from NBP API");
        }
    }
}
