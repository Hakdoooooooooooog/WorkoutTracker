package com.workouttracker.main.controller.Auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workouttracker.main.entities.UsersEntity;
import com.workouttracker.main.service.Implementations.UsersServiceImpl;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class UsersAuthController {
    public final UsersServiceImpl usersService;

    @PostMapping("/login")
    public ResponseEntity<String> Login(@RequestBody UsersEntity user) {
        // Implement login logic
        return ResponseEntity.ok("User logged in successfully");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> Signup(@RequestBody UsersEntity user) {
        try {
            usersService.createUser(user);
            return ResponseEntity.ok("User created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> Logout(@RequestBody UsersEntity user) {
        // Implement logout logic
        return ResponseEntity.ok("User logged out successfully");
    }

}
