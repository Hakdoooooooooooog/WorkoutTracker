package com.workouttracker.main.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.workouttracker.main.dtos.ApiResponseDto;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        List<Map<String, String>> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            Map<String, String> errorDetail = new HashMap<>();
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorDetail.put("field", fieldName);
            errorDetail.put("message", errorMessage);
            errors.add(errorDetail);
        });

        ApiResponseDto response = new ApiResponseDto(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                null,
                errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());
        ApiResponseDto response = new ApiResponseDto(
                HttpStatus.NOT_FOUND,
                "User not found",
                ex.getMessage(),
                null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ApiResponseDto> handleDuplicateUserException(EntityExistsException ex) {
        log.warn("Duplicate user: {}", ex.getMessage());
        ApiResponseDto response = new ApiResponseDto(
                HttpStatus.CONFLICT,
                "User already exists",
                ex.getMessage(),
                null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponseDto> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        ApiResponseDto response = new ApiResponseDto(
                HttpStatus.UNAUTHORIZED,
                "Authentication failed",
                ex.getMessage(),
                null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDto> handleRuntimeException(RuntimeException ex) {
        log.error("Unexpected error occurred", ex);
        ApiResponseDto response = new ApiResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                ex.getMessage(),
                null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Invalid input: {}", ex.getMessage());
        ApiResponseDto response = new ApiResponseDto(
                HttpStatus.BAD_REQUEST,
                "Invalid input",
                ex.getMessage(),
                null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleGenericException(Exception ex) {
        log.error("Critical error occurred", ex);
        ApiResponseDto response = new ApiResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                "An unexpected error occurred",
                null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
