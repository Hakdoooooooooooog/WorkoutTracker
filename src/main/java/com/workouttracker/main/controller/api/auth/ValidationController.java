package com.workouttracker.main.controller.api.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.workouttracker.main.service.Implementations.Users.UsersServiceImpl;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/api/validate")
public class ValidationController {

    private final UsersServiceImpl usersService;

    @GetMapping("/username")
    public ResponseEntity<String> validateUsername(@RequestParam String username) {
        try {
            usersService.validateUsername(username);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok("<span class='error'>" + e.getMessage() + "</span>");
        }
    }

    @GetMapping("/email")
    public ResponseEntity<String> validateEmail(@RequestParam String email) {
        try {
            usersService.validateEmail(email);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok("<span class='error'>" + e.getMessage() + "</span>");
        }
    }

}
