package com.workouttracker.main.service.Implementations.Users;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.workouttracker.main.dtos.Users.UsersDto;
import com.workouttracker.main.entities.User.UsersEntity;
import com.workouttracker.main.exception.DuplicateUserException;
import com.workouttracker.main.exception.UserNotFoundException;
import com.workouttracker.main.mapper.UsersMapper;
import com.workouttracker.main.repositories.UsersRepository;
import com.workouttracker.main.service.Implementations.JWTServiceImpl;
import com.workouttracker.main.service.Interfaces.Users.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final JWTServiceImpl jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

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
    public UsersDto getUserById(UUID userId) throws UserNotFoundException {
        return usersRepository.findById(userId)
                .map(usersMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

    }

    @Override
    public UsersEntity createUser(UsersEntity user) throws DuplicateUserException {
        if (usersRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateUserException("User with email " + user.getEmail() + " already exists");
        }

        if (usersRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateUserException("User with username " + user.getUsername() + " already exists");
        }

        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        log.info("Creating new user: {}", user.getUsername());
        return usersRepository.save(user);
    }

    @Override
    public UsersEntity updateUser(UUID userId, UsersEntity user) throws UserNotFoundException {
        return usersRepository.findById(userId)
                .map(existingUser -> {
                    usersMapper.updateEntityFromDto(user, existingUser);
                    // updatedAt will be auto-updated by @PreUpdate
                    log.info("User updated successfully: {}", existingUser.getUsername());

                    return usersRepository.save(existingUser);
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    @Override
    public void deleteUser(UUID userId) throws UserNotFoundException {
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        usersRepository.delete(user);
        log.info("User deleted successfully: {}", user.getUsername());
    }

    public String loginUser(String username, String password) throws UserNotFoundException {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (authentication.isAuthenticated()) {
            log.info("User logged in successfully: {}", username);
            return jwtService.generateToken(username);
        }

        return null;
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void logoutUser(String email) {
        // For stateless JWT/Basic Auth, logout is typically handled client-side
        log.info("Logout requested for user with email: {}", email);
        // You could implement token blacklisting here if using JWT
    }

    public UsersDto getUserByEmail(String email) {
        return usersRepository.findByEmail(email)
                .map(usersMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public UsersDto getUserByUsername(String username) {
        return usersRepository.findByUsername(username)
                .map(usersMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

}
