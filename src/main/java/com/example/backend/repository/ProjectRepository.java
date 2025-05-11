package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Project;
import com.example.backend.entity.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Custom query to find all projects for a specific user
    @Query("SELECT p FROM Project p WHERE p.id IN (SELECT f.project.id FROM Performance f WHERE f.user = :user)")
    List<Project> findByUser(@Param("user") User user);
}
