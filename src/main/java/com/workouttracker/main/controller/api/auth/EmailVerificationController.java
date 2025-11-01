package com.workouttracker.main.controller.api.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workouttracker.main.service.Implementations.Users.UsersServiceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/email")
public class EmailVerificationController {

    private final UsersServiceImpl usersService;

    @PostMapping("/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam(required = false) String email) {

        try {
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("<span class='error'>Email is required</span>");
            }

            String code = usersService.sendEmailVerificationCode(email.trim());
            log.info("Verification code generated: {} (This should be sent via email in production)", code);
            return ResponseEntity.ok(
                    "<span class='text-green-600'>Verification code sent successfully! Check console for code (development mode): "
                            + code + "</span>");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("<span class='error'>" + e.getMessage() + "</span>");
        } catch (Exception e) {
            log.error("Failed to send verification code", e);
            return ResponseEntity.internalServerError()
                    .body("<span class='error'>Failed to send verification code. Please try again.</span>");
        }
    }
}
