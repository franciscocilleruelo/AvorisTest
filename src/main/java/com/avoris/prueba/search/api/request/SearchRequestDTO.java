package com.avoris.prueba.search.api.request;

import java.util.List;

import com.avoris.prueba.search.api.validation.CheckInBeforeCheckOut;
import com.avoris.prueba.search.api.validation.ValidRealDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Represents a search request containing details needed for hotel availability search.
 * This class is validated using Jakarta Bean Validation annotations.
 * <p>
 * Validation rules:
 * <ul>
 *   <li>{@code hotelId} must not be empty.</li>
 *   <li>{@code checkIn} and {@code checkOut} must be valid real dates in "dd/MM/yyyy" format.</li>
 *   <li>The {@code checkIn} date must be before the {@code checkOut} date.</li>
 *   <li>{@code ages} must not be null or empty.</li>
 * </ul>
 * </p>
 */
@CheckInBeforeCheckOut(message = "Check-in date must be before check-out date")
public record SearchRequestDTO(
        @NotEmpty(message = "hotelId is required")
        String hotelId,

        @NotNull(message = "checkIn date is required")
        @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "checkIn date must be in dd/MM/yyyy format")
        @ValidRealDate(message = "checkIn date must be a real date in the format dd/MM/yyyy")
        String checkIn,

        @NotNull(message = "checkOut date is required")
        @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "checkOut date must be in dd/MM/yyyy format")
        @ValidRealDate(message = "checkOut date must be a real date in the format dd/MM/yyyy")
        String checkOut,

        @NotNull(message = "Ages cannot be null")
        @NotEmpty(message = "Ages list cannot be empty")
        List<Integer> ages
) {
}
