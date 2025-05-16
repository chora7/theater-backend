package com.example.backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class DepartmentRequest {
    private String name;
    private String category;
    private List<Long> assignedUsersIds;
}
