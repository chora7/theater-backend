package com.example.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.entity.User;
import com.example.backend.exception.UserAlreadyExists;
import com.example.backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired private UserRepository repo;

    @Autowired private PasswordEncoder passwordEncoder;

    public User register (User user, boolean isAdmin) {

        boolean userExists = repo.existsByUsername(user.getUsername());

        if (userExists) throw new UserAlreadyExists("User already exists");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (isAdmin) {
            user.setRoles(List.of("ROLE_USER", "ROLE_ADMIN"));
        } else {
            user.setRoles(List.of("ROLE_USER"));
        }
        return repo.save(user);
    }
}
