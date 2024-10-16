package com.taskpro.backend.dto;

import com.taskpro.backend.entity.Project;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private List<ProjectCollaboratorDto> collaborators;
    private List<TaskSummaryDto> tasks;

    public ProjectDto(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.createdAt = project.getCreatedAt();
        this.collaborators = project.getCollaborators().stream()
                .map(ProjectCollaboratorDto::new)
                .toList();
        this.tasks = project.getTasks().stream()
                .map(TaskSummaryDto::new)
                .toList();
    }
}
