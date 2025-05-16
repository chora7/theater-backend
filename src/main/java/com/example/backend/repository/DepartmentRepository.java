package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Department;
import com.example.backend.entity.User;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.assignedUsers")
    List<Department> findAllWithUsers();

    List<Department> findByAssignedUsersContaining(@Param("userId") User user);
}
