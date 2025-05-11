package com.example.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    
    @GetMapping("/login")
    public String loginPage () {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard (Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        return "dashboard";
    }
    
}
