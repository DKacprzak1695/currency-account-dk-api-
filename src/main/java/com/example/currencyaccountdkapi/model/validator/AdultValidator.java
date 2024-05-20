package com.example.currencyaccountdkapi.model.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AdultValidator implements ConstraintValidator<Adult, String> {

    @Override
    public void initialize(Adult constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String pesel, ConstraintValidatorContext context) {
        if (pesel == null || pesel.length() != 11) {
            addConstraintViolation(context, "PESEL must be 11 characters long");
            return false;
        }

        LocalDate birthDate = extractBirthDateFromPesel(pesel);
        if (birthDate == null) {
            addConstraintViolation(context, "Invalid PESEL format");
            return false;
        }

        if (ChronoUnit.YEARS.between(birthDate, LocalDate.now()) < 18) {
            addConstraintViolation(context, "User must be an adult");
            return false;
        }

        return true;
    }

    private LocalDate extractBirthDateFromPesel(String pesel) {
        try {
            int year = Integer.parseInt(pesel.substring(0, 2));
            int month = Integer.parseInt(pesel.substring(2, 4));
            int day = Integer.parseInt(pesel.substring(4, 6));

            if (month > 80) {
                year += 1800;
                month -= 80;
            } else if (month > 60) {
                year += 2200;
                month -= 60;
            } else if (month > 40) {
                year += 2100;
                month -= 40;
            } else if (month > 20) {
                year += 2000;
                month -= 20;
            } else {
                year += 1900;
            }

            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            return null;
        }
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}

