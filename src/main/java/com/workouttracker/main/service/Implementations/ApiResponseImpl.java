package com.workouttracker.main.service.Implementations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.workouttracker.main.dtos.ApiResponseDto;
import com.workouttracker.main.service.Interfaces.ApiResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ApiResponseImpl implements ApiResponse {
    private String status;
    private String message;
    private String error;
    private Object data;

    @Override
    public ResponseEntity<ApiResponseDto> error(String message, String error, Object data) {
        this.status = "Error";
        this.message = message;
        this.error = error;
        this.data = data;

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(build());
    }

    @Override
    public ResponseEntity<ApiResponseDto> success(String message, Object data) {
        this.status = "Success";
        this.message = message;
        this.error = null;
        this.data = data;
        return ResponseEntity.ok(build());
    }

    @Override
    public ApiResponseDto build() {
        return new ApiResponseDto(status, message, error, data);
    }
}
