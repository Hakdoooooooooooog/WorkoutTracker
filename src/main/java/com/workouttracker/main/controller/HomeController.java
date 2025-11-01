package com.workouttracker.main.controller;

import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String getIndexPage(Model model) {
        // Populate navigation items for the header fragment
        List<Map<String, String>> navItems = List.of(
                Map.of("url", "/", "title", "Home"),
                Map.of("url", "/dashboard", "title", "Dashboard"));

        model.addAttribute("navItems", navItems);

        return "index"; // HTML TEMPLATE NAME
    }

    @GetMapping("/dashboard")
    public String getDashboardPage(Model model) {
        // Populate navigation items for the header fragment
        List<Map<String, String>> navItems = List.of(
                Map.of("url", "/", "title", "Home"),
                Map.of("url", "/dashboard", "title", "Dashboard"));

        model.addAttribute("navItems", navItems);

        return "features/Dashboard/dashboard"; // HTML TEMPLATE NAME
    }

}