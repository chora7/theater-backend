package com.example.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.DepartmentInfo;
import com.example.backend.dto.DepartmentRequest;
import com.example.backend.entity.Department;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.DepartmentService;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    

    @Autowired private DepartmentService service;
    @Autowired private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> listAll () {
        try {
            List<DepartmentInfo> departmentDtos = DepartmentInfo.fromDepartmentList(service.getAllForCurrentUser());
            return ResponseEntity.ok(new ApiResponse("Departments retrieved successfully", departmentDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Error retrieving departments", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get (@PathVariable Long id) {
        try {
            DepartmentInfo departmentDto = new DepartmentInfo(service.getById(id));
            return ResponseEntity.ok(new ApiResponse("Department retrieved sucessfully", departmentDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error retrieving department", e.getMessage()));
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> create (@RequestBody DepartmentRequest request) {
        try {
            Department newDepartment = new Department();
            newDepartment.setName(request.getName());
            newDepartment.setCategory(request.getCategory());

            if (request.getAssignedUsersIds() != null && !request.getAssignedUsersIds().isEmpty()) {
                List<User> assignees = userRepository.findAllById(request.getAssignedUsersIds());

                if (assignees.size() != request.getAssignedUsersIds().size()) {
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse("Some user IDs are invalid", null));
                }

                newDepartment.setAssignedUsers(assignees);
            } else {
            newDepartment.setAssignedUsers(new ArrayList<>());
            }
            
            service.create(newDepartment);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Department created", new DepartmentInfo(newDepartment)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Could not create department", e.getMessage()));
        }
    }

    @PostMapping("/{departmentId}/assign/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> assignDepartmentToUser(@PathVariable Long departmentId, @PathVariable Long userId) {
        try {
            service.assignDepartmentToUser(departmentId, userId);
            return ResponseEntity.ok(new ApiResponse("User assigned to department", userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Could not assigne user to department", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update (@PathVariable Long id, @RequestBody Department department) {
        try {
            Department updatedDepartment = service.update(id, department);

            if (updatedDepartment != null) {
                return ResponseEntity.ok(new ApiResponse("Department updated", updatedDepartment));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Department not found", updatedDepartment));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error retrieving department", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete (@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(new ApiResponse("Department deleated", id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error deleating department", e.getMessage()));
        }
    }
}
