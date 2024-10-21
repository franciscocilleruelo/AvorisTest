package com.avoris.prueba.search.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to validate that the check-in date is before the check-out date in a hotel search request.
 * <p>
 * This annotation is applied to classes (typically DTOs) that contain both check-in and check-out dates.
 * The corresponding {@link CheckInBeforeCheckOutValidator} class implements the logic to enforce this rule.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * {@code
 * @CheckInBeforeCheckOut(message = "Check-in date must be before check-out date")
 * public class SearchRequestDTO {
 *     private String checkIn;
 *     private String checkOut;
 *     // getters and setters
 * }
 * }
 * </pre>
 */
@Target(ElementType.TYPE) // Applies to classes (e.g., DTOs)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckInBeforeCheckOutValidator.class)
public @interface CheckInBeforeCheckOut {

    /**
     * The default error message when the validation fails.
     * 
     * @return the error message to be shown when the check-in date is not before the check-out date.
     */
    String message() default "Check-in date must be before check-out date";

    /**
     * Groups allow the annotation to be applied to specific validation groups.
     * 
     * @return the validation groups the annotation belongs to.
     */
    Class<?>[] groups() default {};

    /**
     * Payload can be used by clients of the Jakarta Bean Validation API to assign custom payload objects to a constraint.
     * 
     * @return the payload associated with the annotation.
     */
    Class<? extends Payload>[] payload() default {};
}
