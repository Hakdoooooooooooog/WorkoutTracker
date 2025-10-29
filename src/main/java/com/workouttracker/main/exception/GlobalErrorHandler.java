package com.workouttracker.main.exception;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.workouttracker.main.dtos.Users.LoginRequest;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        model.addAttribute("error", "Validation failed");
        model.addAttribute("errors", ex.getBindingResult().getAllErrors());

        return "error"; // HTML TEMPLATE NAME
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentialsException(BadCredentialsException ex, Model model) {
        model.addAttribute("error", "Invalid username or password");
        model.addAttribute("loginError", "Invalid username or password"); // For login form display
        model.addAttribute("user", new LoginRequest()); // Add empty login request
        model.addAttribute("pageTitle", "Login");

        return "features/auth/login"; // Updated template path
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException ex, Model model) {
        model.addAttribute("error", "Entity not found");
        model.addAttribute("errors", ex.getMessage());

        return "features/auth/login"; // HTML TEMPLATE NAME
    }

    @ExceptionHandler(EntityExistsException.class)
    public String handleEntityExistsException(EntityExistsException ex, Model model) {
        model.addAttribute("error", "Entity already exists");
        model.addAttribute("errors", ex.getMessage());

        return "features/auth/register"; // HTML TEMPLATE NAME
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        model.addAttribute("error", "Invalid input");
        model.addAttribute("errors", ex.getMessage());

        return "error"; // HTML TEMPLATE NAME
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("error", "An unexpected error occurred");
        model.addAttribute("errors", ex.getMessage());

        return "error"; // HTML TEMPLATE NAME
    }
}
