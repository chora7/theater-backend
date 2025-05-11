package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Performance;
import com.example.backend.entity.PerformanceId;
import com.example.backend.entity.Project;
import com.example.backend.entity.User;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, PerformanceId> {

    // find performances by user id
    List<Performance> findByUser_Id(Long userId);

    // find for a specific user
    List<Performance> findByUser (User user);

    // find performances by project id
    List<Performance> findByProject_Id(Long projectId);

    // find by a specific project
    List<Performance> findByProject (Project project);

    // find by a specific user and a specific project
    List<Performance> findByUserAndProject(User user, Project project);

    // exists by user id and project id
    boolean existsByUser_IdAndProject_Id(Long userId, Long projectId);

    // exists by a specific user and a specific project
    boolean existsByUserAndProject(User user, Project project);
}
