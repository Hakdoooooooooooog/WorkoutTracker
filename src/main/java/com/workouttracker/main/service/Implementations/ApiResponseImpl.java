package com.workouttracker.main.service.Implementations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.workouttracker.main.dtos.ApiResponseDto;
import com.workouttracker.main.service.Interfaces.ApiResponse;

@Service
public class ApiResponseImpl implements ApiResponse {

    @Override
    public ResponseEntity<ApiResponseDto> error(String message, String error, Object data) {
        ApiResponseDto responseDto = new ApiResponseDto(HttpStatus.BAD_REQUEST, message);

        responseDto.setError(error);
        responseDto.setData(data);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @Override
    public ResponseEntity<ApiResponseDto> success(String message, Object data) {
        ApiResponseDto responseDto = new ApiResponseDto(HttpStatus.OK, message, data);
        return ResponseEntity.ok(responseDto);
    }
}
