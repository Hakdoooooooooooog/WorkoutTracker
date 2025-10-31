package com.workouttracker.main.config.JWT.handlers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workouttracker.main.dtos.ApiResponseDto;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Check if the request is an HTMX/AJAX request (common header)
        if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With")) ||
                request.getHeader("HX-Request") != null) {

            // HTMX/AJAX Error Handling - return HTML that can be inserted into hx-target
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            response.setContentType("text/plain;charset=UTF-8");

            // Return simple error span that matches validation error format
            response.getWriter().write("<span class='error'>Access denied</span>");
            return;
        }

        // Default API/Non-HTMX Error Handling (as you had before)
        log.warn("Access denied: {}", accessDeniedException.getMessage());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
        // ... write JSON response body ...

        // For simplicity, using a hardcoded JSON string or skipping the full code:
        ApiResponseDto errorResponse = new ApiResponseDto(
                HttpStatus.FORBIDDEN,
                "Forbidden",
                "You don't have permission to access this resource",
                null);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
