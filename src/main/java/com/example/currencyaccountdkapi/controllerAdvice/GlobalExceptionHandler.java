package com.example.currencyaccountdkapi.controllerAdvice;

import com.example.currencyaccountdkapi.excpetion.InvalidAmountException;
import com.example.currencyaccountdkapi.excpetion.NoExchangeRateDataException;
import com.example.currencyaccountdkapi.excpetion.UnsupportedCurrencyCodeException;
import com.example.currencyaccountdkapi.excpetion.UserNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoExchangeRateDataException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNoExchangeRateDataException(NoExchangeRateDataException exception) {
        log.error("No exchange rate data: {}", exception.getMessage(), exception);
        ErrorResponse errorResponse = new ErrorResponse(NOT_FOUND.value(), exception.getMessage(), LocalDateTime.now(ZoneOffset.UTC));
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        log.error("User not found: {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), LocalDateTime.now(ZoneOffset.UTC));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception exception) {
        log.error("An unexpected error occurred: {}", exception.getMessage(), exception);
        ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR.value(), exception.getMessage(), LocalDateTime.now(ZoneOffset.UTC));
        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error("Illegal argument: {}", exception.getMessage(), exception);
        ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST.value(), exception.getMessage(), LocalDateTime.now(ZoneOffset.UTC));
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAmountException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidAmountException(InvalidAmountException exception) {
        log.error("Invalid amount: {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST.value(), exception.getMessage(), LocalDateTime.now(ZoneOffset.UTC));
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedCurrencyCodeException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleUnsupportedCurrencyCodeException(UnsupportedCurrencyCodeException exception) {
        log.error("Unsupported currency code: {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST.value(), exception.getMessage(), LocalDateTime.now(ZoneOffset.UTC));
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("Validation failed: {}", errorMessage, exception);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exception) {
        String errorMessage = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.error("Constraint violation: {}", errorMessage, exception);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
