package com.avoris.prueba.search.api.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class ValidRealDateTest {

    private Validator validator;
    
    // Clase auxiliar de prueba para utilizar la anotación @ValidRealDate
    private static class TestClassWithDate {
        @ValidRealDate(message = "Invalid date format or non-existent date")
        private final String date;

        public TestClassWithDate(String date) {
            this.date = date;
        }
    }

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDate() {
        // Caso de fecha válida
        TestClassWithDate validDateObject = new TestClassWithDate("15/10/2023");
        Set<ConstraintViolation<TestClassWithDate>> violations = validator.validate(validDateObject);

        // No debería haber violaciones
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidDateFormat() {
        // Caso de formato de fecha inválido
        TestClassWithDate invalidDateFormatObject = new TestClassWithDate("2023-10-15");
        Set<ConstraintViolation<TestClassWithDate>> violations = validator.validate(invalidDateFormatObject);

        // Debería haber una violación
        assertEquals(1, violations.size());
        ConstraintViolation<TestClassWithDate> violation = violations.iterator().next();
        assertEquals("Invalid date format or non-existent date", violation.getMessage());
    }

    @Test
    void testNonExistentDate() {
        // Caso de fecha inexistente
        TestClassWithDate nonExistentDateObject = new TestClassWithDate("31/02/2023");
        Set<ConstraintViolation<TestClassWithDate>> violations = validator.validate(nonExistentDateObject);

        // Debería haber una violación
        assertEquals(1, violations.size());
        ConstraintViolation<TestClassWithDate> violation = violations.iterator().next();
        assertEquals("Invalid date format or non-existent date", violation.getMessage());
    }

    
}
