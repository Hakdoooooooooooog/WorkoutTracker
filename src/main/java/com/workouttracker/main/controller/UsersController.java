package com.workouttracker.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workouttracker.main.dtos.ApiResponseDto;
import com.workouttracker.main.dtos.Users.UsersDto;
import com.workouttracker.main.service.Implementations.ApiResponseImpl;
import com.workouttracker.main.service.Interfaces.UsersService;

import lombok.AllArgsConstructor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UsersController {
    public final UsersService usersService;
    public final ApiResponseImpl apiResponse;

    @GetMapping("{id}")
    public ResponseEntity<UsersDto> getUserById(@PathVariable String id) {
        try {
            UUID userId = UUID.fromString(id);

            return ResponseEntity.ok(usersService.getUserById(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDto> deleteUser(@PathVariable String id) {
        try {
            UUID userId = UUID.fromString(id);
            usersService.deleteUser(userId);
            return apiResponse.success("User deleted successfully", userId);
        } catch (IllegalArgumentException e) {
            return apiResponse.error("Invalid user ID", e.getMessage(), id);
        } catch (RuntimeException e) {
            return apiResponse.error("Invalid user ID", e.getMessage(), id);
        }
    }

}
