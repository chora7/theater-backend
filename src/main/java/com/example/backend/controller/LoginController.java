package com.example.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.AuthRequest;
import com.example.backend.security.JwtUtil;
import com.example.backend.service.TokenBlacklistService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class LoginController {
    
    @Autowired private AuthenticationManager authManager;
    @Autowired private TokenBlacklistService tokenBlacklistService;
    @Autowired private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody AuthRequest request) {
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            String token = jwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(new ApiResponse("Login successful", "Bearer " + token));
        } catch (BadCredentialsException e) {
            logger.warn("Login failed: Invalid credentials for username {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid credentials", null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout (HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
        }
    
        return ResponseEntity.ok(new ApiResponse("Logged out successfully", null));
    }
}
