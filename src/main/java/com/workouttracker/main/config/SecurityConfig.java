package com.workouttracker.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.ignoringRequestMatchers("/logout"));

                http.authorizeHttpRequests(auth -> auth
                                .requestMatchers("/login", "/register", "/logout", "/api/validate/**")
                                .permitAll() // Allow home page, registration/login, logout and static resources without
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

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring().requestMatchers("/main.css", "/css/**", "/js/**",
                                "/images/**");
        }
}
