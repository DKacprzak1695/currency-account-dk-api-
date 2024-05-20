package com.example.currencyaccountdkapi;

import com.example.currencyaccountdkapi.excpetion.InvalidAmountException;
import com.example.currencyaccountdkapi.excpetion.UnsupportedCurrencyCodeException;
import com.example.currencyaccountdkapi.excpetion.UserNotFoundException;
import com.example.currencyaccountdkapi.model.UserAccount;
import com.example.currencyaccountdkapi.model.request.CreateUserAccountRequest;
import com.example.currencyaccountdkapi.model.request.ExchangeRequest;
import com.example.currencyaccountdkapi.model.response.UserAccountResponse;
import com.example.currencyaccountdkapi.repository.UserAccountRepository;
import com.example.currencyaccountdkapi.service.CurrencyClient;
import com.example.currencyaccountdkapi.service.UserAccountServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceImplTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private CurrencyClient currencyService;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    private CreateUserAccountRequest createUserAccountRequest;
    private ExchangeRequest exchangeRequest;
    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        createUserAccountRequest = new CreateUserAccountRequest();
        createUserAccountRequest.setFirstName("John");
        createUserAccountRequest.setLastName("Doe");
        createUserAccountRequest.setPesel("85020312345");
        createUserAccountRequest.setPlnBalance(new BigDecimal("1000.00"));

        exchangeRequest = new ExchangeRequest();
        exchangeRequest.setFromCurrency("PLN");
        exchangeRequest.setToCurrency("USD");
        exchangeRequest.setAmount(new BigDecimal("100.00"));

        userAccount = new UserAccount();
        userAccount.setId(1L);
        userAccount.setFirstName("John");
        userAccount.setLastName("Doe");
        userAccount.setPesel("85020312345");
        userAccount.setPlnBalance(new BigDecimal("1000.00"));
        userAccount.setUsdBalance(new BigDecimal("0.00"));
    }

    @AfterEach
    void tearDown() {
        reset(userAccountRepository, currencyService);
    }

    @Test
    void should_register_user_successfully() {
        when(userAccountRepository.findByPesel(anyString())).thenReturn(Optional.empty());
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(userAccount);

        UserAccountResponse response = userAccountService.registerUser(createUserAccountRequest);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("85020312345", response.getPesel());
        assertEquals(new BigDecimal("1000.00"), response.getPlnBalance());
        assertEquals(new BigDecimal("0.00"), response.getUsdBalance());

        verify(userAccountRepository).findByPesel(anyString());
        verify(userAccountRepository).save(any(UserAccount.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        when(userAccountRepository.findByPesel(anyString())).thenReturn(Optional.of(userAccount));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userAccountService.registerUser(createUserAccountRequest);
        });

        assertEquals("Account with given PESEL already exists.", exception.getMessage());

        verify(userAccountRepository).findByPesel(anyString());
        verify(userAccountRepository, never()).save(any(UserAccount.class));
    }

    @Test
    void should_return_user_account_by_pesel_successfully() {
        when(userAccountRepository.findByPesel(anyString())).thenReturn(Optional.of(userAccount));

        UserAccountResponse response = userAccountService.getUserAccountByPesel("85020312345");

        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("85020312345", response.getPesel());
        assertEquals(new BigDecimal("1000.00"), response.getPlnBalance());
        assertEquals(new BigDecimal("0.00"), response.getUsdBalance());

        verify(userAccountRepository).findByPesel(anyString());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userAccountRepository.findByPesel(anyString())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userAccountService.getUserAccountByPesel("85020312345");
        });

        assertEquals("User not found for PESEL: 85020312345", exception.getMessage());

        verify(userAccountRepository).findByPesel(anyString());
    }

    @Test
    void shouldExchangeCurrencySuccessfully() {
        when(userAccountRepository.findByPesel(anyString())).thenReturn(Optional.of(userAccount));
        when(currencyService.getUsdExchangeRate()).thenReturn(new BigDecimal("4.00"));
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Upewnij się, że save() zwraca obiekt, który otrzymał jako argument

        UserAccountResponse response = userAccountService.exchangeCurrency("85020312345", exchangeRequest);

        assertNotNull(response);
        assertEquals(0, new BigDecimal("900.00").compareTo(response.getPlnBalance()));
        assertEquals(0, new BigDecimal("25.00").compareTo(response.getUsdBalance()));

        verify(userAccountRepository).findByPesel(anyString());
        verify(userAccountRepository).save(any(UserAccount.class));
        verify(currencyService).getUsdExchangeRate();

        reset(userAccountRepository, currencyService);

        userAccount.setPlnBalance(new BigDecimal("900.00"));
        userAccount.setUsdBalance(new BigDecimal("25.00"));

        exchangeRequest.setFromCurrency("USD");
        exchangeRequest.setToCurrency("PLN");
        exchangeRequest.setAmount(new BigDecimal("25.00"));

        when(userAccountRepository.findByPesel(anyString())).thenReturn(Optional.of(userAccount));
        when(currencyService.getUsdExchangeRate()).thenReturn(new BigDecimal("4.00"));
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Upewnij się, że save() zwraca obiekt, który otrzymał jako argument

        response = userAccountService.exchangeCurrency("85020312345", exchangeRequest);

        assertNotNull(response);
        assertEquals(0, new BigDecimal("1000.00").compareTo(response.getPlnBalance()));
        assertEquals(0, new BigDecimal("0.00").compareTo(response.getUsdBalance()));

        verify(userAccountRepository).findByPesel(anyString());
        verify(userAccountRepository).save(any(UserAccount.class));
        verify(currencyService).getUsdExchangeRate();
    }

    @Test
    void shouldThrowExceptionWhenUnsupportedCurrencyPair() {
        exchangeRequest.setFromCurrency("EUR");
        exchangeRequest.setToCurrency("PLN");

        UnsupportedCurrencyCodeException exception = assertThrows(UnsupportedCurrencyCodeException.class, () -> {
            userAccountService.exchangeCurrency("85020312345", exchangeRequest);
        });

        assertEquals("Unsupported currency code: EUR or PLN", exception.getMessage());

        verify(userAccountRepository, never()).findByPesel(anyString());
        verify(userAccountRepository, never()).save(any(UserAccount.class));
        verify(currencyService, never()).getUsdExchangeRate();
    }


    @Test
    void shouldThrowExceptionWhenNegativeExchangeAmount() {
        exchangeRequest.setAmount(new BigDecimal("-100.00"));

        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            userAccountService.exchangeCurrency("85020312345", exchangeRequest);
        });

        assertEquals("Amount must be greater than 0.01", exception.getMessage());

        verify(userAccountRepository, never()).findByPesel(anyString());
        verify(userAccountRepository, never()).save(any(UserAccount.class));
        verify(currencyService, never()).getUsdExchangeRate();
    }
}
