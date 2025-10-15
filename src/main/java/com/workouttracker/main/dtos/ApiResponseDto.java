package com.workouttracker.main.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto {
    private String status;
    private String message;
    private String error;
    private Object data;

    // Constructor for success responses (without error)
    public ApiResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Constructor for success responses with data (without error)
    public ApiResponseDto(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
