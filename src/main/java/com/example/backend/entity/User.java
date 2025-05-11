package com.example.backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.CascadeType;
// for user role
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;

@Entity
@Data
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @JsonIgnore
    @ManyToMany(mappedBy = "assignedUsers")
    private List<Department> assignedDepartments;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Performance> performances;

    public boolean hasRole(String role) {
        return roles.contains("ROLE_" + role);
    }
}
