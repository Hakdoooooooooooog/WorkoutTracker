package com.workouttracker.main.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.workouttracker.main.service.Implementations.JWTServiceImpl;
import com.workouttracker.main.service.Interfaces.Users.UsersDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private String username;
    private String jwtToken;

    @Autowired
    private JWTServiceImpl jwtService;

    @Autowired
    ApplicationContext applicationContext;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        this.jwtToken = extractJwtToken(request);

        // Only process if token exists
        if (this.jwtToken != null) {
            try {
                this.username = jwtService.extractUsername(this.jwtToken);

                if (this.username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // Load user details
                    UserDetails userDetails = applicationContext.getBean(UsersDetailsService.class)
                            .loadUserByUsername(this.username);

                    if (jwtService.validateToken(this.jwtToken, userDetails)) {
                        // Set the user details in the security context
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        // Set the details
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Set the authentication in the security context
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.debug("User '{}' authenticated successfully via JWT", this.username);
                    } else {
                        log.warn("JWT token validation failed for user: {}", this.username);
                    }
                }
            } catch (Exception e) {
                // Log and continue - invalid tokens simply won't authenticate
                log.warn("JWT authentication failed: {}", e.getMessage());
                // User remains unauthenticated, will get 401 from AuthorizationFilter
            }
        }

        // Continue with the next filter in the chain
        filterChain.doFilter(request, response);
    }

    private String extractJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
