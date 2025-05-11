package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Department;
import com.example.backend.entity.User;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByAssignedUsersContaining(@Param("userId") User user);
}
