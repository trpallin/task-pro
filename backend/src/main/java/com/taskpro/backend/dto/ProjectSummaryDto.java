package com.taskpro.backend.dto;

import com.taskpro.backend.entity.Project;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectSummaryDto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public ProjectSummaryDto(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.createdAt = project.getCreatedAt();
    }
}
