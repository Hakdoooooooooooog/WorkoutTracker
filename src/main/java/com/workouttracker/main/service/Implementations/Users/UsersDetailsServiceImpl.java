package com.workouttracker.main.service.Implementations.Users;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.workouttracker.main.entities.User.UserPrincipal;
import com.workouttracker.main.entities.User.UsersEntity;
import com.workouttracker.main.repositories.UsersRepository;
import com.workouttracker.main.service.Interfaces.Users.UsersDetailsService;

public class UsersDetailsServiceImpl implements UsersDetailsService {

    UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity userDetails = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserPrincipal(userDetails);
    }

}
