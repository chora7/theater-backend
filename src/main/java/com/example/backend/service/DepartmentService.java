package com.example.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.backend.entity.Department;
import com.example.backend.entity.User;
import com.example.backend.exception.CustomAccessDeniedException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.DepartmentRepository;
import com.example.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class DepartmentService {
    
    @Autowired private DepartmentRepository repository;
    @Autowired private UserRepository userRepository;

    public List<Department> getAllForCurrentUser () {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRoles().contains("ROLE_ADMIN")) {
            return repository.findAllWithUsers();
        }

        return repository.findByAssignedUsersContaining(user);
    }

    @Transactional
    public Department getById (Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Department department = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if (user.getRoles().contains("ROLE_ADMIN")) {
            return department;
        }

        if (!department.getAssignedUsers().contains(user)) {
            throw new CustomAccessDeniedException("You are not assigned to this department");
        }

        return department;
    }

    @Transactional
    public Department create (Department department) {
        return repository.save(department);
    }

    @Transactional
    public void assignDepartmentToUser (Long departmentId, Long userId) {
        Department department = repository.findById(departmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        department.getAssignedUsers().add(user);
        repository.save(department);
    }

    public Department update (Long id, Department updated) {
        Department existing = getById(id);
        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        return repository.save(existing);
    }

    public void delete (Long id) {
        repository.deleteById(id);
    }
}
