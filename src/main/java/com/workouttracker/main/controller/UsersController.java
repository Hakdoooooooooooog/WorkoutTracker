package com.workouttracker.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workouttracker.main.dtos.UsersDto;
import com.workouttracker.main.service.Interfaces.UsersService;

import lombok.AllArgsConstructor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UsersController {
    public final UsersService usersService;

    @GetMapping("{id}")
    public ResponseEntity<UsersDto> getUserById(@PathVariable String id) {
        try {
            UUID userId = UUID.fromString(id);

            return ResponseEntity.ok(usersService.getUserById(userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
