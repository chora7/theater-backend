package com.example.backend.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.backend.entity.Performance;
import com.example.backend.entity.PerformanceId;
import com.example.backend.entity.Project;
import com.example.backend.entity.User;
import com.example.backend.enums.PerformanceStatus;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.PerformanceRepository;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PerformanceService {
    
    private final PerformanceRepository repository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public PerformanceService(PerformanceRepository performanceRepository,
                              UserRepository userRepository,
                              ProjectRepository projectRepository) {

        this.repository = performanceRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public List<Performance> getAllForCurrentUser () {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.hasRole("ADMIN")) {
            System.out.println("Admin fetching all performances");
            return repository.findAll();
        }

        System.out.println("Username: " + user.getUsername());
        System.out.println("Roles: " + user.getRoles());
        return repository.findByUser(user);
    }

    @Transactional
    public Performance assignUserToProject (Long userId, Long projectId, String role,
                                            String specialization, PerformanceStatus status) {
                                                
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        PerformanceId id = new PerformanceId(user.getId(), project.getId());

        if (repository.existsById(id)) {
            throw new IllegalStateException("User already assigned to this project");
        }

        Performance performance = new Performance();
        performance.setId(id);
        performance.setUser(user);
        performance.setProject(project);
        performance.setRole(role);
        performance.setSpecialization(specialization);
        performance.setStatus(status);

        return repository.save(performance);
    }

    public boolean isUserAssignedToProject (Long userId, Long projectId) {
        return repository.existsByUser_IdAndProject_Id(userId, userId);
    }

    public List<Performance> getPerformancesByUser (Long userId) {
        return repository.findByUser_Id(userId);
    }

    public List<Performance> getPerformancesByProject (Long projectId) {
        return repository.findByProject_Id(projectId);
    }

    public List<Performance> getAllPerformancesForAdmin () {
        return repository.findAll();
    }
}
