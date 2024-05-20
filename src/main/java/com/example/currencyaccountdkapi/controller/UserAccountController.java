package com.example.currencyaccountdkapi.controller;

import com.example.currencyaccountdkapi.model.request.ExchangeRequest;
import com.example.currencyaccountdkapi.model.request.CreateUserAccountRequest;
import com.example.currencyaccountdkapi.model.response.UserAccountResponse;
import com.example.currencyaccountdkapi.service.UserAccountServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-accounts")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountServiceImpl userAccountService;

    @PostMapping
    public ResponseEntity<UserAccountResponse> registerUser(@RequestBody @Valid CreateUserAccountRequest command) {
        UserAccountResponse userAccountDTO = userAccountService.registerUser(command);
        return new ResponseEntity<>(userAccountDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{pesel}")
    public ResponseEntity<UserAccountResponse> getAccountInfo(@PathVariable String pesel) {
        UserAccountResponse userAccountDTO = userAccountService.getUserAccountByPesel(pesel);
        return userAccountDTO != null ? new ResponseEntity<>(userAccountDTO, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{pesel}/exchange")
    public ResponseEntity<UserAccountResponse> exchangeCurrency(@PathVariable String pesel, @RequestBody @Valid ExchangeRequest request) {
        UserAccountResponse userAccountDTO = userAccountService.exchangeCurrency(pesel, request);
        return new ResponseEntity<>(userAccountDTO, HttpStatus.OK);
    }
}
