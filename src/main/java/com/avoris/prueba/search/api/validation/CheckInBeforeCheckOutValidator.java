package com.avoris.prueba.search.api.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.avoris.prueba.search.api.request.SearchRequestDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator to ensure that the check-in date is before the check-out date.
 * <p>
 * This class implements the {@link ConstraintValidator} interface to enforce the {@link CheckInBeforeCheckOut} constraint.
 * It is responsible for comparing the check-in and check-out dates from the {@link SearchRequestDTO} to ensure that the 
 * check-in date occurs before the check-out date.
 * </p>
 * <p>
 * The check-in and check-out dates are expected to be in the format "dd/MM/yyyy". If either date is null or cannot be 
 * parsed, the validation returns {@code true} and lets other constraints (like {@code @NotNull}) handle the validation of null values.
 * </p>
 */
public class CheckInBeforeCheckOutValidator implements ConstraintValidator<CheckInBeforeCheckOut, SearchRequestDTO> {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Validates that the check-in date is before the check-out date in a {@link SearchRequestDTO}.
     * <p>
     * If the check-in and check-out dates are both present and valid, it checks whether the check-in date is earlier than
     * the check-out date. If either of the dates is null or the format is incorrect, this validation method allows 
     * other validations (like {@link jakarta.validation.constraints.NotNull}) to handle those cases.
     * </p>
     *
     * @param request the {@link SearchRequestDTO} containing the check-in and check-out dates.
     * @param context the {@link ConstraintValidatorContext} for validation.
     * @return {@code true} if the check-in date is before the check-out date or if the dates are null/invalid, {@code false} otherwise.
     */
    @Override
    public boolean isValid(SearchRequestDTO request, ConstraintValidatorContext context) {
        if (request == null || request.checkIn() == null || request.checkOut() == null) {
            return true; // Let @NotNull handle null checks
        }

        try {
            LocalDate checkInDate = LocalDate.parse(request.checkIn(), DATE_FORMAT);
            LocalDate checkOutDate = LocalDate.parse(request.checkOut(), DATE_FORMAT);
            // Ensure check-in date is before check-out date
            return checkInDate.isBefore(checkOutDate);
        } catch (DateTimeParseException ex) {
            // Invalid date format, allow other validations to handle this
            return true;
        }
    }
}
