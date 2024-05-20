package com.example.currencyaccountdkapi.model.request;

import com.example.currencyaccountdkapi.model.validator.Adult;
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
public class CreateUserAccountRequest {

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotBlank(message = "PESEL is mandatory")
    @Adult()
    private String pesel;

    @NotNull(message = "Initial PLN balance is mandatory")
    @DecimalMin(value = "0.0", message = "Initial PLN balance must be greater than or equal to zero")
    private BigDecimal plnBalance;

    @NotNull(message = "Initial USD balance is mandatory")
    @DecimalMin(value = "0.0", message = "Initial USD balance must be greater than or equal to zero")
    private BigDecimal usdBalance = BigDecimal.ZERO;
}
