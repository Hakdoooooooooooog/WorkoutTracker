package com.workouttracker.main.service.Interfaces;

import org.springframework.http.ResponseEntity;

import com.workouttracker.main.dtos.ApiResponseDto;

public interface ApiResponse {
    ResponseEntity<ApiResponseDto> error(String message, String error, Object data);

    ResponseEntity<ApiResponseDto> success(String message, Object data);

    ApiResponseDto build();

}
