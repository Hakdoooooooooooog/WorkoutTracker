package com.workouttracker.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

                http.authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/v1/auth/**").permitAll() // Allow registration/login without auth
                                .anyRequest().authenticated());

                // Form login default
                // http.formLogin(Customizer.withDefaults());

                // HTTP Basic authentication for postman etc...
                http.httpBasic(Customizer.withDefaults());

                // Stateless session management for every request
                http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                return http.build();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder());
                return provider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // @Bean
        // public UserDetailsService userDetailsService() {

        // UserDetails user =
        // User.withDefaultPasswordEncoder().username("test").password("password").roles("USER")
        // .build();
        // return new InMemoryUserDetailsManager(user);
        // }
}
