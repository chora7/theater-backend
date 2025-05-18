package com.example.backend.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

@Entity
@Data
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "roles")
    private String rolesString;

    @JsonIgnore
    @ManyToMany(mappedBy = "assignedUsers")
    private List<Department> assignedDepartments;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Performance> performances;

    public void setRoles(List<String> roles) {
        this.rolesString =  String.join(",", roles);
    }

    @JsonProperty("roles")
    public List<String> getRoles() {
        if (rolesString == null || rolesString.isBlank()) {
            return new ArrayList<>();
        }

        return Arrays.stream(rolesString.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .collect(Collectors.toList());
    }

    public boolean hasRole(String role) {
        return getRoles().contains(role);
    }
}
