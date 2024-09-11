package com.taskpro.backend.dto;

import com.taskpro.backend.entity.ProjectCollaborator;
import com.taskpro.backend.enums.CollaboratorRoleEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectCollaboratorDto {
    private Long projectId;
    private Long userId;
    private CollaboratorRoleEnum role;
    private LocalDateTime createdAt;

    public ProjectCollaboratorDto(ProjectCollaborator projectCollaborator) {
        this.projectId = projectCollaborator.getProject().getId();
        this.userId = projectCollaborator.getUser().getId();
        this.role = projectCollaborator.getRole();
        this.createdAt = projectCollaborator.getCreatedAt();
    }
}
