package com.workouttracker.main.controller.api.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.workouttracker.main.service.Implementations.Users.UsersServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/validate")
public class ValidationController {

    @Autowired
    private UsersServiceImpl usersService;

    @PostMapping("/username")
    public ResponseEntity<String> validateUsername(@RequestParam String username) throws InterruptedException {
        try {
            usersService.validateUsername(username);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok("<span class='error'>" + e.getMessage() + "</span>");
        }
    }

    @PostMapping("/email")
    public ResponseEntity<String> validateEmail(@RequestParam String email) throws InterruptedException {
        try {
            usersService.validateEmail(email);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok("<span class='error'>" + e.getMessage() + "</span>");
        }
    }

}
