# Currency Account Service API

## Overview

> This service handles account registration, currency exchange between subaccounts in PLN and USD, 
> and retrieval of account information. It ensures user validation, unique PESEL for accounts, and uses the NBP API 
> for live exchange rates. Custom exceptions and unit tests are implemented for robustness.

## Features

> - **User Account Creation**: Validates adult users and unique PESEL.
> - **Currency Exchange**: Exchanges between PLN and USD using live rates.
> - **Account Information**: Retrieves user account details.
> - **Validation and Error Handling**: Custom exceptions for invalid inputs.
> - **Testing**: Unit tests with Mockito.

## Requirements

> - **Java**: OpenJDK 17
> - **Build Tool**: Maven (equipped with Maven Wrapper, hence no configuration is needed)
> - **Framework**: Spring Boot 3.2.5
> - **JPA**: Hibernate for ORM (Object-Relational Mapping)
> - **Mockito**: Framework for unit testing with mock objects