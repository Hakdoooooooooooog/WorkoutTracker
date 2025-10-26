package com.workouttracker.main.dtos;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto {
    private HttpStatusCode status;
    private String message;
    private String error;
    private Object data;

    // Constructor for success responses (without error)
    public ApiResponseDto(HttpStatusCode status, String message) {
        this.status = status;
        this.message = message;
    }

    // Constructor for success responses with data (without error)
    public ApiResponseDto(HttpStatusCode status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Constructor for error responses with error details
    public ApiResponseDto(HttpStatusCode status, String message, String error, Object data) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.data = data;
    }
}
