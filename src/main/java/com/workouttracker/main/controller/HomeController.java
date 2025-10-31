package com.workouttracker.main.controller;

import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@AllArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String getIndexPage(Model model) {
        // TODO: Implement index page logic
        return "index"; // HTML TEMPLATE NAME
    }

    @GetMapping("/dashboard")
    public String getDashboardPage(Model model) {
        // TODO: Implement dashboard logic
        return "dashboard"; // HTML TEMPLATE NAME
    }

}