package com.example.currencyaccountdkapi.repository;

import com.example.currencyaccountdkapi.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByPesel(String pesel);
}
