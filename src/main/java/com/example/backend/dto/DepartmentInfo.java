package com.example.backend.dto;

import java.util.List;

import com.example.backend.entity.Department;
import com.example.backend.entity.User;

import lombok.Data;

@Data
public class DepartmentInfo {
    private Long id;
    private String name;
    private List<String> userNames;

    public DepartmentInfo (Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.userNames = department.getAssignedUsers()
                                   .stream()
                                   .map(User::getUsername)
                                   .toList();
    }

    public static List<DepartmentInfo> fromDepartmentList (List<Department> departments) {
        return departments.stream()
                          .map(DepartmentInfo::new)
                          .toList();
    }
}