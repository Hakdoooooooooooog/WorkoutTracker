package com.workouttracker.main.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.workouttracker.main.dtos.Users.ForgotPasswordRequest;
import com.workouttracker.main.dtos.Users.ResetPasswordRequest;
import com.workouttracker.main.service.Implementations.Users.UsersServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PasswordResetController {

    private final UsersServiceImpl usersService;

    @GetMapping("/forgot-password")
    public String getForgotPasswordPage(Model model) {
        model.addAttribute("pageTitle", "Forgot Password");
        model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequest());
        return "/features/Authentication/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String postForgotPasswordPage(@Valid @ModelAttribute ForgotPasswordRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Forgot Password");
            model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "forgotPasswordRequest", bindingResult);
            return "/features/Authentication/forgot-password";
        }

        try {
            usersService.sendPasswordResetCode(request.getEmail());
            redirectAttributes.addFlashAttribute("successMessage",
                    "A verification code has been sent to your email. Please check your inbox.");
            redirectAttributes.addFlashAttribute("email", request.getEmail());
            return "redirect:/reset-password";
        } catch (EntityNotFoundException e) {
            model.addAttribute("pageTitle", "Forgot Password");
            model.addAttribute("emailError", "No account found with that email address");
            return "/features/Authentication/forgot-password";
        } catch (Exception e) {
            model.addAttribute("pageTitle", "Forgot Password");
            model.addAttribute("emailError", "Failed to send reset code: " + e.getMessage());
            return "/features/Authentication/forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String getResetPasswordPage(Model model) {
        model.addAttribute("pageTitle", "Reset Password");

        // Create reset password request, prepopulating email if available from flash
        // attributes
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        if (model.containsAttribute("email")) {
            resetPasswordRequest.setEmail((String) model.getAttribute("email"));
        }

        model.addAttribute("resetPasswordRequest", resetPasswordRequest);
        return "/features/Authentication/reset-password";
    }

    @PostMapping("/reset-password")
    public String postResetPasswordPage(@Valid @ModelAttribute ResetPasswordRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Reset Password");
            model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "resetPasswordRequest", bindingResult);
            return "/features/Authentication/reset-password";
        }

        try {
            usersService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Password reset successfully! Please login with your new password.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("pageTitle", "Reset Password");
            model.addAttribute("resetError", e.getMessage());
            return "/features/Authentication/reset-password";
        } catch (Exception e) {
            model.addAttribute("pageTitle", "Reset Password");
            model.addAttribute("resetError", "Failed to reset password: " + e.getMessage());
            return "/features/Authentication/reset-password";
        }
    }
}
