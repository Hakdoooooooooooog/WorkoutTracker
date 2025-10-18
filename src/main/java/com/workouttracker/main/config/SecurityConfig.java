package com.workouttracker.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.workouttracker.main.service.Interfaces.Users.UsersDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        UsersDetailsService userDetailsService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable());

                http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

                // Form login default
                http.formLogin(Customizer.withDefaults());

                // HTTP Basic authentication for postman etc...
                http.httpBasic(Customizer.withDefaults());

                // Stateless session management for every request
                http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                return http.build();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
                provider.setUserDetailsService(userDetailsService);
                return provider;
        }
}
