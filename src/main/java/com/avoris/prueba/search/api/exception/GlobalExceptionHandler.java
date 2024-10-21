package com.avoris.prueba.search.api.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler to handle and respond to various exceptions in the application.
 * <p>
 * This class provides centralized exception handling across the entire application.
 * It handles generic exceptions, validation errors, and runtime exceptions, 
 * and provides meaningful error responses with appropriate HTTP status codes.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	/**
     * Default constructor for {@link GlobalExceptionHandler}.
     * <p>
     * Initializes the global exception handler for the application.
     * No specific initialization is required for this class.
     * </p>
     */
    public GlobalExceptionHandler() {
    	super();
        // Default constructor, no specific initialization required.
    }

    /**
     * Handles generic exceptions that are not explicitly handled elsewhere in the application.
     * <p>
     * This method catches all unhandled exceptions and returns a standardized error response 
     * with a message, timestamp, and the exception details. The HTTP status code is set to 500.
     * </p>
     * 
     * @param ex the exception that was thrown.
     * @param request the current web request.
     * @return a {@link ResponseEntity} containing the error details and HTTP status code 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Error inesperado");
        body.put("details", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles validation errors when method arguments are not valid.
     * <p>
     * This method is overridden from {@link ResponseEntityExceptionHandler} to customize the validation error response.
     * It captures all validation errors for method arguments (such as from {@code @Valid}) and formats them into 
     * a map of field names and corresponding error messages. The HTTP status code is set to 400.
     * </p>
     * 
     * @param ex the exception thrown when method argument validation fails.
     * @param headers HTTP headers to be sent with the response.
     * @param status the HTTP status code.
     * @param request the current web request.
     * @return a {@link ResponseEntity} containing the validation errors and HTTP status code 400.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());

        // Gather field errors
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles runtime exceptions that occur in the application.
     * <p>
     * This method captures runtime exceptions and returns an error message with a 500 status code.
     * Runtime exceptions may occur due to unforeseen issues during the execution of the application.
     * </p>
     * 
     * @param ex the runtime exception that was thrown.
     * @return a {@link ResponseEntity} containing the error message and HTTP status code 500.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>("Internal Server Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
