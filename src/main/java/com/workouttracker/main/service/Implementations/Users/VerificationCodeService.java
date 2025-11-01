package com.workouttracker.main.service.Implementations.Users;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VerificationCodeService {
    
    // In-memory storage for verification codes (email -> code data)
    // In production, use Redis or database with TTL
    private final Map<String, CodeData> verificationCodes = new HashMap<>();
    
    private static final int EXPIRATION_MINUTES = 15;
    
    private static class CodeData {
        String code;
        LocalDateTime expiresAt;
        String purpose; // "registration" or "password-reset"
        
        CodeData(String code, LocalDateTime expiresAt, String purpose) {
            this.code = code;
            this.expiresAt = expiresAt;
            this.purpose = purpose;
        }
    }
    
    /**
     * Generate a random 6-digit verification code
     */
    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
    
    /**
     * Generate and store verification code for email registration
     */
    public String generateRegistrationCode(String email) {
        String code = generateCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
        
        verificationCodes.put(email.toLowerCase(), new CodeData(code, expiresAt, "registration"));
        
        log.info("Generated registration verification code for email: {}", email);
        // In production: Send email here
        log.info("Verification code (for development): {}", code);
        
        return code;
    }
    
    /**
     * Generate and store verification code for password reset
     */
    public String generatePasswordResetCode(String email) {
        String code = generateCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
        
        verificationCodes.put(email.toLowerCase(), new CodeData(code, expiresAt, "password-reset"));
        
        log.info("Generated password reset verification code for email: {}", email);
        // In production: Send email here
        log.info("Verification code (for development): {}", code);
        
        return code;
    }
    
    /**
     * Verify code for registration
     */
    public boolean verifyRegistrationCode(String email, String code) {
        return verifyCode(email, code, "registration");
    }
    
    /**
     * Verify code for password reset
     */
    public boolean verifyPasswordResetCode(String email, String code) {
        return verifyCode(email, code, "password-reset");
    }
    
    /**
     * Verify code with purpose check
     */
    private boolean verifyCode(String email, String code, String purpose) {
        CodeData codeData = verificationCodes.get(email.toLowerCase());
        
        if (codeData == null) {
            log.warn("No verification code found for email: {}", email);
            return false;
        }
        
        if (!codeData.purpose.equals(purpose)) {
            log.warn("Code purpose mismatch for email: {}. Expected: {}, Found: {}", 
                     email, purpose, codeData.purpose);
            return false;
        }
        
        if (LocalDateTime.now().isAfter(codeData.expiresAt)) {
            log.warn("Verification code expired for email: {}", email);
            verificationCodes.remove(email.toLowerCase());
            return false;
        }
        
        if (!codeData.code.equals(code)) {
            log.warn("Invalid verification code for email: {}", email);
            return false;
        }
        
        // Code is valid, remove it to prevent reuse
        verificationCodes.remove(email.toLowerCase());
        log.info("Verification code validated successfully for email: {}", email);
        return true;
    }
    
    /**
     * Check if a code exists for the email (for any purpose)
     */
    public boolean hasValidCode(String email) {
        CodeData codeData = verificationCodes.get(email.toLowerCase());
        if (codeData == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(codeData.expiresAt)) {
            verificationCodes.remove(email.toLowerCase());
            return false;
        }
        
        return true;
    }
    
    /**
     * Remove code for email
     */
    public void removeCode(String email) {
        verificationCodes.remove(email.toLowerCase());
        log.info("Removed verification code for email: {}", email);
    }
}
