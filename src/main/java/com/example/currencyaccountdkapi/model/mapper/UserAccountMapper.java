package com.example.currencyaccountdkapi.model.mapper;

import com.example.currencyaccountdkapi.model.UserAccount;
import com.example.currencyaccountdkapi.model.request.CreateUserAccountRequest;
import com.example.currencyaccountdkapi.model.response.UserAccountResponse;

import java.math.BigDecimal;

public class UserAccountMapper {

    public static UserAccount toUserAccount(CreateUserAccountRequest command) {
        UserAccount userAccount = new UserAccount();
        userAccount.setFirstName(command.getFirstName());
        userAccount.setLastName(command.getLastName());
        userAccount.setPesel(command.getPesel());
        userAccount.setPlnBalance(command.getPlnBalance());
        userAccount.setUsdBalance(BigDecimal.ZERO);
        return userAccount;
    }

    public static UserAccountResponse toUserAccountDTO(UserAccount userAccount) {
        UserAccountResponse dto = new UserAccountResponse();
        dto.setFirstName(userAccount.getFirstName());
        dto.setLastName(userAccount.getLastName());
        dto.setPesel(userAccount.getPesel());
        dto.setPlnBalance(userAccount.getPlnBalance());
        dto.setUsdBalance(userAccount.getUsdBalance());
        return dto;
    }
}
