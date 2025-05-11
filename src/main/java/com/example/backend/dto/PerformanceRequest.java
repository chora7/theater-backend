package com.example.backend.dto;

import com.example.backend.enums.PerformanceStatus;

import lombok.Data;

@Data
public class PerformanceRequest {
    private Long userId;
    private Long projectId;
    private String role;
    private String specialization;
    private PerformanceStatus status;
}
