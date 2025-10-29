package com.workouttracker.main.service.Implementations.Users;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.workouttracker.main.config.PermissionManager;
import com.workouttracker.main.dtos.Users.RegisterRequest;
import com.workouttracker.main.dtos.Users.UsersDto;
import com.workouttracker.main.entities.Users.UsersEntity;
import com.workouttracker.main.mapper.UsersMapper;
import com.workouttracker.main.repositories.Users.UsersRepository;
import com.workouttracker.main.service.Implementations.JWTServiceImpl;
import com.workouttracker.main.service.Interfaces.Users.UsersService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
    public UsersDto getUserById(UUID userId) {
        return usersRepository.findById(userId)
                .map(usersMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

    }

    @Override
    public UsersEntity createUser(UsersEntity user) {
        int userPermission = PermissionManager.PERM_USER; // Default permission

        if (usersRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EntityExistsException("User with email: " + user.getEmail() + " already exists");
        }

        if (usersRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new EntityExistsException("User with username: " + user.getUsername() + " already exists");
        }

        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        // Set default permission for new users
        user.setPermission(userPermission);

        log.info("Creating new user: {}", user.getUsername());
        return usersRepository.save(user);
    }

    @Override
    public UsersEntity updateUser(UUID userId, UsersEntity user) {
        return usersRepository.findById(userId)
                .map(existingUser -> {
                    usersMapper.updateEntityFromDto(user, existingUser);
                    // updatedAt will be auto-updated by @PreUpdate
                    log.info("User updated successfully: {}", existingUser.getUsername());

                    return usersRepository.save(existingUser);
                })
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
    }

    @Override
    public void deleteUser(UUID userId) {
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        usersRepository.delete(user);
        log.info("User deleted successfully: {}", user.getUsername());
    }

    public String loginUser(String username, String password) {
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

    // public void logoutUser(String email) {

    // log.info("Logout requested for user with email: {}", email);
    // // You could implement token blacklisting here if using JWT
    // }

    public UsersDto getUserByEmail(String email) {
        return usersRepository.findByEmail(email)
                .map(usersMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    public UsersDto getUserByUsername(String username) {
        return usersRepository.findByUsername(username)
                .map(usersMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    @Override
    public void registerUser(RegisterRequest registerRequest) {
        // Check if user already exists
        if (usersRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new EntityExistsException("Username already exists: " + registerRequest.getUsername());
        }

        if (usersRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new EntityExistsException("Email already exists: " + registerRequest.getEmail());
        }

        // Create new user entity
        UsersEntity user = new UsersEntity();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPermission(PermissionManager.PERM_USER);

        // Save user
        usersRepository.save(user);
        log.info("User registered successfully: {}", registerRequest.getUsername());
    }

}
