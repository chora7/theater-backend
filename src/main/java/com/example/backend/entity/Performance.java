package com.example.backend.entity;

import com.example.backend.enums.PerformanceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "performance")
public class Performance {

    @EmbeddedId
    private PerformanceId id;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("projectId")
    private Project project;

    @Column(name = "role")
    private String role;
    @Column(name = "specialization")
    private String specialization;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PerformanceStatus status;
}
