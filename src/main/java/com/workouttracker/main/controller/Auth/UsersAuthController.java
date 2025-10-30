package com.workouttracker.main.controller.auth;

import com.workouttracker.main.config.JWT.JwtFilter;
import com.workouttracker.main.dtos.Users.LoginRequest;
import com.workouttracker.main.dtos.Users.RegisterRequest;

import com.workouttracker.main.service.Implementations.Users.UsersServiceImpl;

import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class UsersAuthController {

    private final UsersServiceImpl usersService;
    private final JwtFilter jwtFilter;

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("pageTitle", "Login");
        model.addAttribute("formTitle", "Login");

        // Create login request, prepopulating username if available from flash
        // attributes
        LoginRequest loginRequest = new LoginRequest();
        if (model.containsAttribute("username")) {
            loginRequest.setUsername((String) model.getAttribute("username"));
        }

        model.addAttribute("user", loginRequest);
        return "/features/auth/login"; // HTML TEMPLATE NAME
    }

    @PostMapping("/login")
    public String postLoginPage(@Valid @ModelAttribute LoginRequest user,
            BindingResult bindingResult,
            Model model,
            HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Login");
            model.addAttribute("formTitle", "Login");
            model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "user", bindingResult);
            return "/features/auth/login"; // HTML TEMPLATE NAME
        }

        if (model.containsAttribute("logoutMessage")) {
            model.addAttribute("logoutMessage", model.getAttribute("logoutMessage"));
        }

        // Attempt login
        String token = usersService.loginUser(user.getUsername(), user.getPassword());

        if (token != null) {
            jwtFilter.setJwtCookie(response, token);

            return "redirect:/"; // Redirect to home page
        } else {
            model.addAttribute("pageTitle", "Login");
            model.addAttribute("formTitle", "Login");
            model.addAttribute("loginError", "Invalid username or password");
            return "/features/auth/login";
        }
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("pageTitle", "Register");
        model.addAttribute("formTitle", "Register");
        model.addAttribute("user", new RegisterRequest());
        return "/features/auth/register";
    }

    @PostMapping("/register")
    public String postRegisterPage(@Valid @ModelAttribute RegisterRequest user,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("formTitle", "Register");
            model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "user", bindingResult);
            return "/features/auth/register";
        }

        // Attempt registration
        try {
            usersService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            redirectAttributes.addFlashAttribute("username", user.getUsername());
            return "redirect:/login";
        } catch (EntityExistsException e) {
            // Handle duplicate username/email as field errors
            if (e.getMessage().contains("Username")) {
                bindingResult.rejectValue("username", "duplicate", "Username already exists");
            } else if (e.getMessage().contains("Email")) {
                bindingResult.rejectValue("email", "duplicate", "Email already exists");
            }
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("formTitle", "Register");
            model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "user", bindingResult);
            return "/features/auth/register";
        } catch (Exception e) {
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("formTitle", "Register");
            model.addAttribute("registerError", "Registration failed: " + e.getMessage());
            return "/features/auth/register";
        }
    }

}
