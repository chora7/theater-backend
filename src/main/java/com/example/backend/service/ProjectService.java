package com.example.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.backend.entity.Performance;
import com.example.backend.entity.PerformanceId;
import com.example.backend.entity.Project;
import com.example.backend.entity.User;
import com.example.backend.enums.PerformanceStatus;
import com.example.backend.exception.CustomAccessDeniedException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.PerformanceRepository;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.backend.enums.ProjectStatus;

@Service
public class ProjectService {
    
    @Autowired
    private ProjectRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    public List<Project> getAllForCurrentUser () {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.hasRole("ROLE_ADMIN")) {
            System.out.println("Admin fetching all projects");
            return repository.findAll();
        }

        System.out.println("Username: " + user.getUsername());
        System.out.println("Roles: " + user.getRoles());
        return repository.findByUser(user);

    }

    public Project getByIdForCurrentUser (Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Project project = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (user.hasRole("ROLE_ADMIN")) {
            return project;
        }

        boolean isAssigned = performanceRepository.existsByUser_IdAndProject_Id(user.getId(), project.getId());
        if (!isAssigned) {
            throw new CustomAccessDeniedException("You are not assigned to this project");
        }

        return project;
    }

    @Transactional
    public Project create (Project project) {
        if (project.getStatus() == null) {
            project.setStatus(ProjectStatus.PLANNED); // or whatever your default should be
        }
        return repository.save(project);
    }

    @Transactional
    public void assignProjectToUser (Long projectId, Long userId) {
        Project project = repository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
        Performance performance = new Performance();
        performance.setId(new PerformanceId(user.getId(), project.getId()));
        performance.setUser(user);
        performance.setProject(project);
        performance.setRole("Unassigned");
        performance.setSpecialization("General");
        performance.setStatus(PerformanceStatus.ASSIGNED);

        performanceRepository.save(performance);
    }

    public Project update (Long id, Project updated) {
        Project existing = getByIdForCurrentUser(id);
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setVisible(updated.isVisible());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setStatus(updated.getStatus()); 
        return repository.save(existing);
    }

    public void delete (Long id) {
        repository.deleteById(id);
    }
}
