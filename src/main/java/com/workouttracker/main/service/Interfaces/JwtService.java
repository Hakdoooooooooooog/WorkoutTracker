package com.workouttracker.main.service.Interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(String username);

    boolean validateToken(String token, UserDetails userDetails);

    String extractUsername(String token);

}
