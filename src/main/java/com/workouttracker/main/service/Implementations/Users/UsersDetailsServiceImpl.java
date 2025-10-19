package com.workouttracker.main.service.Implementations.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.workouttracker.main.entities.User.UserPrincipal;
import com.workouttracker.main.entities.User.UsersEntity;
import com.workouttracker.main.repositories.UsersRepository;
import com.workouttracker.main.service.Interfaces.Users.UsersDetailsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsersDetailsServiceImpl implements UsersDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to authenticate user: {}", username);

        UsersEntity user = usersRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Authentication failed: User '{}' not found", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        return new UserPrincipal(user);
    }

}
