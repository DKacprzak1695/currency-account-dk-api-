package com.example.currencyaccountdkapi.service;

import com.example.currencyaccountdkapi.excpetion.InvalidAmountException;
import com.example.currencyaccountdkapi.excpetion.UnsupportedCurrencyCodeException;
import com.example.currencyaccountdkapi.excpetion.UserNotFoundException;
import com.example.currencyaccountdkapi.model.UserAccount;
import com.example.currencyaccountdkapi.model.mapper.UserAccountMapper;
import com.example.currencyaccountdkapi.model.request.ExchangeRequest;
import com.example.currencyaccountdkapi.model.response.UserAccountResponse;
import com.example.currencyaccountdkapi.model.request.CreateUserAccountRequest;
import com.example.currencyaccountdkapi.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final CurrencyClient currencyService;

    @Override
    @Transactional
    public UserAccountResponse registerUser(CreateUserAccountRequest command) {
        userAccountRepository.findByPesel(command.getPesel())
                .ifPresent(account -> {
                    throw new IllegalArgumentException("Account with given PESEL already exists.");
                });

        UserAccount userAccount = UserAccountMapper.toUserAccount(command);
        return UserAccountMapper.toUserAccountDTO(userAccountRepository.save(userAccount));
    }

    @Override
    public UserAccountResponse getUserAccountByPesel(String pesel) {
        return userAccountRepository.findByPesel(pesel)
                .map(UserAccountMapper::toUserAccountDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found for PESEL: " + pesel));
    }

    @Override
    @Transactional
    public UserAccountResponse exchangeCurrency(String pesel, ExchangeRequest request) {
        validateExchangeRequest(request);
        UserAccount userAccount = userAccountRepository.findByPesel(pesel)
                .orElseThrow(() -> new UserNotFoundException("User not found for PESEL: " + pesel));

        BigDecimal exchangeRate = getExchangeRate(request);
        BigDecimal exchangedAmount = request.getAmount().multiply(exchangeRate);

        updateBalances(userAccount, request, exchangedAmount);
        userAccount = userAccountRepository.save(userAccount);

        return UserAccountMapper.toUserAccountDTO(userAccount);
    }

    private BigDecimal getExchangeRate(ExchangeRequest request) {
        return switch (request.getFromCurrency() + "_" + request.getToCurrency()) {
            case "PLN_USD" -> BigDecimal.ONE.divide(currencyService.getUsdExchangeRate(), 4, RoundingMode.HALF_UP);
            case "USD_PLN" -> currencyService.getUsdExchangeRate();
            default -> throw new IllegalArgumentException
                    ("Unsupported currency exchange: " + request.getFromCurrency() + " to " + request.getToCurrency());
        };
    }

    private void updateBalances(UserAccount userAccount, ExchangeRequest request, BigDecimal exchangedAmount) {
        String currencyPair = request.getFromCurrency() + "_" + request.getToCurrency();

        switch (currencyPair) {
            case "PLN_USD" -> {
                if (userAccount.getPlnBalance().compareTo(request.getAmount()) < 0) {
                    throw new IllegalArgumentException("Insufficient PLN balance");
                }
                userAccount.setPlnBalance(userAccount.getPlnBalance().subtract(request.getAmount()));
                userAccount.setUsdBalance(userAccount.getUsdBalance().add(exchangedAmount));
            }
            case "USD_PLN" -> {
                if (userAccount.getUsdBalance().compareTo(request.getAmount()) < 0) {
                    throw new IllegalArgumentException("Insufficient USD balance");
                }
                userAccount.setUsdBalance(userAccount.getUsdBalance().subtract(request.getAmount()));
                userAccount.setPlnBalance(userAccount.getPlnBalance().add(exchangedAmount));
            }
            default -> throw new IllegalArgumentException("Unsupported currency exchange: " + request.getFromCurrency() + " to " + request.getToCurrency());
        }
    }

    private void validateExchangeRequest(ExchangeRequest request) {
        if (request.getAmount().compareTo(new BigDecimal("0.01")) <= 0) {
            throw new InvalidAmountException("Amount must be greater than 0.01");
        }

        if (!isValidCurrencyCode(request.getFromCurrency()) || !isValidCurrencyCode(request.getToCurrency())) {
            throw new UnsupportedCurrencyCodeException("Unsupported currency code: " + request.getFromCurrency() + " or " + request.getToCurrency());
        }
    }


    private boolean isValidCurrencyCode(String currencyCode) {
        return currencyCode.equals("PLN") || currencyCode.equals("USD");
    }
}
