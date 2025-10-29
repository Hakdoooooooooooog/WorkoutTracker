package com.workouttracker.main.service.Interfaces.Users;

import java.util.List;
import java.util.UUID;

import com.workouttracker.main.dtos.Users.RegisterRequest;
import com.workouttracker.main.dtos.Users.UsersDto;
import com.workouttracker.main.entities.Users.UsersEntity;

public interface UsersService {
    List<UsersDto> getAllUsers();

    UsersDto getUserById(UUID userId);

    UsersEntity createUser(UsersEntity user);

    UsersEntity updateUser(UUID userId, UsersEntity user);

    void deleteUser(UUID userId);

    void registerUser(RegisterRequest registerRequest);
}
