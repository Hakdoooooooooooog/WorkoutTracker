package com.workouttracker.main.config.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.workouttracker.main.service.Implementations.JWTServiceImpl;
import com.workouttracker.main.service.Implementations.Users.UsersDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
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
                SecurityContext securityContext = SecurityContextHolder.getContext();

                if (this.username != null && securityContext.getAuthentication() == null) {

                    // Load user details
                    UserDetails userDetails = applicationContext.getBean(UsersDetailsServiceImpl.class)
                            .loadUserByUsername(this.username);

                    if (jwtService.validateToken(this.jwtToken, userDetails)) {
                        // Set the user details in the security context
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        // Set the details (IP address, user agent, etc.)
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Set the authentication in the security context
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        // Token is invalid/expired, clear the cookie
                        clearJwtCookie(response);
                    }
                } else {
                    // Token exists but username couldn't be extracted, clear the cookie
                    clearJwtCookie(response);
                }
            } catch (Exception e) {
                log.error("Error occurred while processing JWT token: {}", e.getMessage());
                // Clear cookie on any error to be safe
                clearJwtCookie(response);
            }
        }

        // Continue with the next filter in the chain
        filterChain.doFilter(request, response);
    }

    private String extractJwtToken(HttpServletRequest request) {
        // First check Authorization header
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        // Then check cookie
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public void clearJwtCookie(HttpServletResponse response) {
        jakarta.servlet.http.Cookie jwtCookie = new jakarta.servlet.http.Cookie("jwtToken", "");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // Expire immediately
        response.addCookie(jwtCookie);
    }

    public void setJwtCookie(HttpServletResponse response, String token) {
        jakarta.servlet.http.Cookie jwtCookie = new jakarta.servlet.http.Cookie("jwtToken", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(30 * 60); // 30 minutes in seconds (matches JWT expiration)
        response.addCookie(jwtCookie);
    }
}
