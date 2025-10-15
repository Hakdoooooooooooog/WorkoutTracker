package com.workouttracker.main.service.Interfaces;

import java.util.List;
import java.util.UUID;

import com.workouttracker.main.dtos.Users.UsersDto;
import com.workouttracker.main.entities.UsersEntity;

public interface UsersService {
    List<UsersDto> getAllUsers();

    UsersDto getUserById(UUID userId);

    UsersEntity createUser(UsersEntity user);

    UsersEntity updateUser(UUID userId, UsersEntity user);

    void deleteUser(UUID userId);
}
