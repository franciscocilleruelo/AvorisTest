package com.avoris.prueba.search.api.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator that checks if a given string represents a real date in the format "dd/MM/yyyy".
 * <p>
 * This validator ensures that invalid dates, such as "31/02/2023", are not accepted. It uses a strict 
 * {@link DateTimeFormatter} to prevent automatic adjustments (e.g., correcting "31/02/2023" to "03/03/2023").
 * </p>
 */
public class RealDateFieldValidator implements ConstraintValidator<ValidRealDate, String> {

    // Configures a strict DateTimeFormatter that does not allow adjustments for invalid dates
    private static final DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("dd/MM/yyyy")
            .parseStrict()  // Enables strict parsing to prevent automatic date adjustment
            .toFormatter();
    
    /**
     * Default constructor for {@link RealDateFieldValidator}.
     * This class does not require any custom initialization.
     */
    public RealDateFieldValidator() {
        // No custom initialization required
    }

    /**
     * Validates if the provided date string is a valid and real date in the "dd/MM/yyyy" format.
     * <p>
     * This method checks if the provided string is null or if it conforms to the "dd/MM/yyyy" format
     * and represents a valid date (e.g., "31/02/2023" would be invalid).
     * </p>
     *
     * @param dateField the date string to validate.
     * @param context the {@link ConstraintValidatorContext} in which the constraint is evaluated.
     * @return true if the date is valid or null, false otherwise.
     */
    @Override
    public boolean isValid(String dateField, ConstraintValidatorContext context) {
        if (dateField == null) {
            return true; // Let @NotNull handle null checks if needed
        }
        return isValidDate(dateField);
    }

    /**
     * Checks if the provided date string is a valid date in the "dd/MM/yyyy" format.
     * <p>
     * This method ensures that the date is both valid and conforms to the provided format.
     * </p>
     * 
     * @param date the date string to check.
     * @return true if the date is valid, false if it's not a real date or cannot be parsed.
     */
    private boolean isValidDate(String date) {
        try {
            // Try to parse the date
            LocalDate parsedDate = LocalDate.parse(date, DATE_FORMAT);

            // Ensure the entered date matches the parsed date (prevents automatic correction)
            String formattedDate = parsedDate.format(DATE_FORMAT);
            return date.equals(formattedDate);
        } catch (DateTimeParseException e) {
            return false; // Invalid date if parsing fails
        }
    }
}
