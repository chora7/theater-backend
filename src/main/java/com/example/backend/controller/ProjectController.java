package com.example.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ApiResponse;
import com.example.backend.entity.Project;
import com.example.backend.service.ProjectService;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired private ProjectService service;

    @GetMapping
    public ResponseEntity<?> listAll () {
        try {
            List<Project> allProjects = service.getAllForCurrentUser();
            return ResponseEntity.ok(new ApiResponse("Successfully fetched all projects", allProjects));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error fetching projects", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get (@PathVariable Long id) {
        try {
            Project project = service.getByIdForCurrentUser(id);
            return ResponseEntity.ok(new ApiResponse("Successfully fetched project", project));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error fetching project", e.getMessage()));
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<?> create (@RequestBody Project project) {
        try {
            Project newProject = service.create(project);
            return ResponseEntity.ok(new ApiResponse("Successfully added project", newProject));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error adding project", e.getMessage()));
        }
    }

    @PostMapping("/{projectId}/assign/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<?> assignUserToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        try {
            service.assignUserToProject(projectId, userId);
            return ResponseEntity.ok(new ApiResponse("Successfully assigned project to user",  null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error assigning project", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> update (@PathVariable Long id, @RequestBody Project project) {
        try {
            Project updatedProject = service.update(id, project);
            return ResponseEntity.ok(new ApiResponse("Successfully updated project",  updatedProject));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating project", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> delete (@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted project",  null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error deliting project", e.getMessage()));
        }   
    }
}
