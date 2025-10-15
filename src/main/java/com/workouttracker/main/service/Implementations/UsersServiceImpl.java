package com.workouttracker.main.service.Implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.workouttracker.main.dtos.UsersDto;
import com.workouttracker.main.entities.UsersEntity;
import com.workouttracker.main.mapper.UsersMapper;
import com.workouttracker.main.repositories.UsersRepository;
import com.workouttracker.main.service.Interfaces.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    public final UsersRepository usersRepository;
    public final UsersMapper usersMapper;

    @Override
    public List<UsersDto> getAllUsers() {
        List<UsersDto> userDTOs = usersRepository.findAll()
                .stream()
                .map(usersMapper::toDto)
                .toList();

        if (userDTOs.isEmpty()) {
            log.warn("No users found");

            return List.of();
        }
        return userDTOs;
    }

    @Override
    public UsersDto getUserById(UUID userId) {
        return usersRepository.findById(userId)
                .map(usersMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));

    }

    @Override
    public UsersEntity createUser(UsersEntity user) {
        user.setId(UUID.randomUUID());

        return usersRepository.save(user);
    }

    @Override
    public UsersEntity updateUser(UUID userId, UsersEntity user) {
        return usersRepository.findById(userId)
                .map(existingUser -> {
                    usersMapper.updateEntityFromDto(user, existingUser);
                    log.info("User updated successfully");

                    return usersRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUser(UUID userId) {
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        usersRepository.delete(user);
        log.info("User deleted successfully");
    }

}
