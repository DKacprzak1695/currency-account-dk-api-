package com.example.currencyaccountdkapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String pesel;

    @Column(nullable = false)
    private BigDecimal plnBalance;

    @Column(nullable = false)
    private BigDecimal usdBalance = BigDecimal.ZERO;
}
