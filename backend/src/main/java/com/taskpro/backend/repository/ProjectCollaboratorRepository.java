package com.taskpro.backend.repository;

import com.taskpro.backend.entity.ProjectCollaborator;
import com.taskpro.backend.entity.id.ProjectCollaboratorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectCollaboratorRepository extends JpaRepository<ProjectCollaborator, ProjectCollaboratorId> {
    Optional<ProjectCollaborator> findByProjectIdAndUserId(Long projectId, Long userId);
}
