package com.example.backend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HomeController {

    // for testing the JWT token and "ROLE_USER"
    // POST http://localhost:8080/auth/register/?isAdmin=true
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/hello")
    public String sayHello (Authentication authentication) {
        if (authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
                return "Hello admin!";
        } else {
            return "Hello, authenticated user!";
        }
    }

    // temporary
    @GetMapping("/debug")
    public String debugAuth () {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "User: " + auth.getName() + ", Roles: " + auth.getAuthorities();
    }
}
