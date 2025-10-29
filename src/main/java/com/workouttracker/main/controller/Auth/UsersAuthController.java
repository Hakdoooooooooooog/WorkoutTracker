package com.workouttracker.main.controller.auth;

import com.workouttracker.main.config.JWT.JwtFilter;
import com.workouttracker.main.dtos.Users.LoginRequest;

import com.workouttracker.main.service.Implementations.Users.UsersServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class UsersAuthController {

    private final UsersServiceImpl usersService;
    private final JwtFilter jwtFilter;

    @GetMapping("/login")
    public String getLoginPage(@ModelAttribute LoginRequest user, Model model) {
        model.addAttribute("pageTitle", "Login");
        return "/features/auth/login"; // HTML TEMPLATE NAME
    }

    @PostMapping("/login")
    public String postLoginPage(@Valid @ModelAttribute LoginRequest user,
            BindingResult bindingResult,
            Model model,
            HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
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
            model.addAttribute("loginError", "Invalid username or password");
            return "/features/auth/login";
        }
    }

}
