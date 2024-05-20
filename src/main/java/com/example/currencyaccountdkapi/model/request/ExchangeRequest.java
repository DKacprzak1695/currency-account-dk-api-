package com.example.currencyaccountdkapi.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRequest {
    @NotBlank(message = "From currency is mandatory")
    private String fromCurrency;

    @NotBlank(message = "To currency is mandatory")
    private String toCurrency;

    @NotNull(message = "Amount is mandatory")
    @DecimalMin(value = "0.01", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;
}
