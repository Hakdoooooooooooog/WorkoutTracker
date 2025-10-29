package com.workouttracker.main.config.JWT.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.workouttracker.main.config.JWT.JwtFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtLogoutHandler implements LogoutHandler {

    @Autowired
    private JwtFilter jwtFilter;

    public JwtLogoutHandler(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        // Clear cookie
        jwtFilter.clearJwtCookie(response);
    }

}
