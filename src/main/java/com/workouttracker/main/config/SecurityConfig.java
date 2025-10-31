package com.workouttracker.main.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.workouttracker.main.config.JWT.JwtAuthenticationEntryPoint;
import com.workouttracker.main.config.JWT.JwtFilter;
import com.workouttracker.main.config.JWT.handlers.JWTSuccessLogoutHandler;
import com.workouttracker.main.config.JWT.handlers.JwtAccessDeniedHandler;
import com.workouttracker.main.config.JWT.handlers.JwtLogoutHandler;
import com.workouttracker.main.service.Interfaces.Users.UsersDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private UsersDetailsService userDetailsService;

        @Autowired
        private JwtFilter jwtFilter;

        @Autowired
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @Autowired
        private JwtAccessDeniedHandler jwtAccessDeniedHandler;

        private final List<String> publicUrls = List.of("/", "/login", "/register", "/logout", "/api/validate/**",
                        "/main.css", "/css/**", "/js/**", "/images/**");

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.ignoringRequestMatchers("/logout"));

                http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(publicUrls.toArray(String[]::new))
                                                .permitAll() // Allow home page, registration/login without
                                                             // authentication
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                                .accessDeniedHandler(jwtAccessDeniedHandler))
                                .logout(logout -> logout
                                                .addLogoutHandler(new JwtLogoutHandler(jwtFilter))
                                                .logoutSuccessHandler(new JWTSuccessLogoutHandler())
                                                .permitAll())
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder());
                return provider;
        }

        @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
