package com.workouttracker.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workouttracker.main.dtos.ApiResponseDto;
import com.workouttracker.main.dtos.Users.UsersDto;
import com.workouttracker.main.service.Implementations.ApiResponseImpl;
import com.workouttracker.main.service.Interfaces.Users.UsersService;

import lombok.AllArgsConstructor;

import java.util.List;
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

    @GetMapping
    public ResponseEntity<ApiResponseDto> getAllUsers() {
        List<UsersDto> users = usersService.getAllUsers();
        return apiResponse.success("Users retrieved successfully", users);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponseDto> getUserById(@PathVariable String id) {

        UUID userId = UUID.fromString(id);
        UsersDto user = usersService.getUserById(userId);
        return apiResponse.success("User retrieved successfully", user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDto> deleteUser(@PathVariable String id) {
        UUID userId = UUID.fromString(id);
        usersService.deleteUser(userId);
        return apiResponse.success("User deleted successfully", userId);
    }

}
