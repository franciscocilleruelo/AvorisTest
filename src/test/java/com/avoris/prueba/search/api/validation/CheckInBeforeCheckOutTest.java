package com.avoris.prueba.search.api.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.avoris.prueba.search.api.request.SearchRequestDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class CheckInBeforeCheckOutTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCheckInBeforeCheckOut() {
        SearchRequestDTO validRequest = new SearchRequestDTO(
            "1234", "01/01/2024", "10/01/2024", Arrays.asList(12, 32, 61, 19)
        );

        Set<ConstraintViolation<SearchRequestDTO>> violations = validator.validate(validRequest);

        // Nos aseguramos e de que no haya violaciones
        assertEquals(0, violations.size(), "Se esperaba que no hubiera violaciones de validación para fechas de check-in y check-out válidas.");
    }

    @Test
    void testInvalidCheckInAfterCheckOut() {
        SearchRequestDTO invalidRequest = new SearchRequestDTO(
            "1234", "15/01/2024", "10/01/2024", Arrays.asList(12, 32, 61, 19)
        );

        Set<ConstraintViolation<SearchRequestDTO>> violations = validator.validate(invalidRequest);

        // Nos aseguramos de que haya una violación
        assertEquals(1, violations.size(), "Se esperaba una violación de validación para fechas de check-in y check-out inválidas.");

        // Verificamos el mensaje de la violación
        ConstraintViolation<SearchRequestDTO> violation = violations.iterator().next();
        assertEquals("Check-in date must be before check-out date", violation.getMessage());
    }

    @Test
    void testCheckInEqualsCheckOut() {
        SearchRequestDTO invalidRequest = new SearchRequestDTO(
            "1234", "10/01/2024", "10/01/2024", Arrays.asList(12, 32, 61, 19)
        );

        Set<ConstraintViolation<SearchRequestDTO>> violations = validator.validate(invalidRequest);

        // Comprobamos de que haya una violación
        assertEquals(1, violations.size(), "Se esperaba una violación de validación para fechas de check-in y check-out iguales.");

        // Verificamos el mensaje de la violación
        ConstraintViolation<SearchRequestDTO> violation = violations.iterator().next();
        assertEquals("Check-in date must be before check-out date", violation.getMessage());
    }

}
