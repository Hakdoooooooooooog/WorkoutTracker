package com.workouttracker.main.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@AllArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String getIndexPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isAuthenticated", authentication.isAuthenticated());
            model.addAttribute("username", authentication.getName());
        }

        return "index"; // HTML TEMPLATE NAMEs
    }
}