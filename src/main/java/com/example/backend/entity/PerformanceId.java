package com.example.backend.entity;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class PerformanceId {

    private Long userId;
    private Long projectId;

    public PerformanceId(Long userId, Long projectId) {
        this.userId = userId;
        this.projectId = projectId;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PerformanceId)) return false;
        PerformanceId that = (PerformanceId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, projectId);
    }
    
}
