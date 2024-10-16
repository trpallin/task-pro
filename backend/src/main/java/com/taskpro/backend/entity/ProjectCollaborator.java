package com.taskpro.backend.entity;

import com.taskpro.backend.entity.id.ProjectCollaboratorId;
import com.taskpro.backend.enums.CollaboratorRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_collaborators")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCollaborator {
    @EmbeddedId
    private ProjectCollaboratorId id = new ProjectCollaboratorId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CollaboratorRoleEnum role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
