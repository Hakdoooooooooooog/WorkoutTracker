package com.workouttracker.main.controller.Auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workouttracker.main.dtos.ApiResponseDto;
import com.workouttracker.main.dtos.Users.LoginRequest;
import com.workouttracker.main.entities.Users.UsersEntity;
import com.workouttracker.main.service.Implementations.ApiResponseImpl;
import com.workouttracker.main.service.Implementations.Users.UsersServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.Map;

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

        String token = usersService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (token != null) {
            Map<String, Object> userInfo = Map.of(
                    "token", token,
                    "user", usersService.getUserByUsername(loginRequest.getUsername()));

            return apiResponse.success("User logged in successfully", userInfo);
        } else {
            return apiResponse.error("Invalid credentials", "Invalid username or password", null);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> Register(@Valid @RequestBody UsersEntity user) {
        usersService.createUser(user);
        return apiResponse.success("User created successfully", null);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto> Logout(@RequestBody UsersEntity user) {
        // Implement logout logic
        usersService.logoutUser(user.getEmail());
        return apiResponse.success("User logged out successfully", null);

    }

}
