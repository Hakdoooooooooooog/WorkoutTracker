package com.workouttracker.main.controller.Auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workouttracker.main.dtos.ApiResponseDto;
import com.workouttracker.main.dtos.Users.LoginRequest;
import com.workouttracker.main.entities.UsersEntity;
import com.workouttracker.main.service.Implementations.ApiResponseImpl;
import com.workouttracker.main.service.Implementations.UsersServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class UsersAuthController {
    public final UsersServiceImpl usersService;
    public final ApiResponseImpl apiResponse;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> Login(@Valid @RequestBody LoginRequest loginRequest) {
        // Implement login logic

        try {
            boolean isCorrect = usersService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

            if (isCorrect) {
                return apiResponse.success("User logged in successfully",
                        usersService.getUserByEmail(loginRequest.getEmail()));
            } else {
                return apiResponse.error("Invalid credentials", "Invalid email or password", null);
            }
        } catch (Exception e) {
            return apiResponse.error("An error occurred:", e.getMessage(), null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> Register(@Valid @RequestBody UsersEntity user) {
        try {
            usersService.createUser(user);
            return apiResponse.success("User created successfully", null);
        } catch (RuntimeException e) {
            return apiResponse.error("Invalid user data:", e.getMessage(), null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto> Logout(@RequestBody UsersEntity user) {
        // Implement logout logic
        try {
            usersService.logoutUser(user.getEmail());
            return apiResponse.success("User logged out successfully", null);
        } catch (Exception e) {
            return apiResponse.error("An error occurred:", e.getMessage(), null);
        }
    }

}
