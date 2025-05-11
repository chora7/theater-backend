package com.example.backend.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.backend.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final TokenBlacklistService tokenBlacklistService;

    @Autowired private UserDetailsService userDetailsService;
    @Autowired private JwtUtil jwtUtil;

    JwtRequestFilter(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal (@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain chain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization"); // Authorization
        String username = null, token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            username = jwtUtil.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken jwtToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                jwtToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                System.out.println("Authenticated user: " + userDetails.getUsername()); // for test

                SecurityContextHolder.getContext().setAuthentication(jwtToken);

                System.out.println("Authentication set in context");
            }
        }
        chain.doFilter(request, response);
    }
}
