package com.taskpro.backend.repository;

import com.taskpro.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p JOIN p.collaborators c WHERE c.user.id = :userId")
    List<Project> findAllByCollaboratorUserId(Long userId);
}
