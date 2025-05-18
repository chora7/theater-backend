package com.example.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.PerformanceRequest;
import com.example.backend.dto.PerformanceUpdateRequest;
import com.example.backend.entity.Department;
import com.example.backend.entity.Performance;
import com.example.backend.entity.PerformanceId;
import com.example.backend.entity.User;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.PerformanceService;

@RestController
@RequestMapping("/api/performances")
public class PerformanceController {
    
    @Autowired
    private PerformanceService service;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> listAll () {
        try {
            List<Performance> performances = service.getAllForCurrentUser();
            return ResponseEntity.ok(new ApiResponse("All performances for current user", performances));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error fetching performances", e.getMessage()));
        }
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> assignUserToProject(@RequestBody PerformanceRequest request) {
        try {
            Performance performance = service.assignUserToProject(
            request.getUserId(),
            request.getProjectId(),
            request.getRole(),
            request.getSpecialization(),
            request.getStatus());

            return ResponseEntity.ok(new ApiResponse("User assigned to project", performance));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error assigning user to project", e.getMessage()));
        }
    }

    @PutMapping("/{userId}/{projectId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update (@PathVariable Long userId,
                                     @PathVariable Long projectId, 
                                     @RequestBody PerformanceUpdateRequest request) {

        try {

            PerformanceId id = new PerformanceId(userId, projectId);
            Performance updatedPerformance = service.update(id, request);

            if (updatedPerformance != null) {
                return ResponseEntity.ok(new ApiResponse("Performance updated", updatedPerformance));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Performance not found", updatedPerformance));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error retrieving performance", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/{projectId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getPerformancesById (@PathVariable Long userId,
                                                            @PathVariable Long projectId) {
        try {
            Performance performance = service.getPerformanceById(new PerformanceId(userId,projectId));
            return ResponseEntity.ok(new ApiResponse("Successfully returned performance based on user ID and project ID", performance));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error fetching performances", e.getMessage()));
        }
    }
    

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getByUser(@PathVariable Long userId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        try {
            User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (!currentUser.hasRole("ADMIN") && currentUser.getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(new ApiResponse("Performances by user", service.getPerformancesByUser(userId)));


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getByProject(@PathVariable Long projectId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        try {
            User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    
            boolean isAdmin = currentUser.hasRole("ADMIN");
    
            if (!isAdmin) {
                boolean isAssigned = service.isUserAssignedToProject(currentUser.getId(), projectId);
                if (!isAssigned) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }

            return ResponseEntity.ok(new ApiResponse("Successfully fetched performances for project", service.getPerformancesByProject(projectId)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }
}
