package com.avoris.prueba.search.api.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom validation annotation to check if a string represents a valid and real date in the format "dd/MM/yyyy".
 * <p>
 * This annotation can be applied to fields and parameters, and it uses {@link RealDateFieldValidator} 
 * to perform the validation logic. It ensures that dates are not only in the correct format but also represent 
 * real, existing dates (e.g., "31/02/2023" would be invalid).
 * </p>
 * 
 * <p>Usage:</p>
 * <pre>
 * {@code
 * @ValidRealDate(message = "Date must be valid and in the format dd/MM/yyyy")
 * private String checkInDate;
 * }
 * </pre>
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })  // Can be applied to fields and parameters
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RealDateFieldValidator.class)
public @interface ValidRealDate {

    /**
     * The default error message returned when the validation fails.
     * <p>
     * The message can be customized at the annotation level, for example:
     * {@code @ValidRealDate(message = "Please enter a valid date in dd/MM/yyyy format")}
     * </p>
     * 
     * @return the error message.
     */
    String message() default "Invalid date format or non-existent date";

    /**
     * Defines the validation groups, which allow this constraint to be part of a group of validations.
     * Groups are used to restrict the set of constraints applied during validation.
     * 
     * @return the groups the constraint belongs to.
     */
    Class<?>[] groups() default {};

    /**
     * Payload for clients to associate custom metadata with the constraint.
     * This feature can be used to carry additional information about the validation failure.
     * 
     * @return the payload for the constraint.
     */
    Class<? extends Payload>[] payload() default {};
}
