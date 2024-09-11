package com.taskpro.backend.service;

import com.taskpro.backend.dto.*;
import com.taskpro.backend.entity.Project;
import com.taskpro.backend.entity.ProjectCollaborator;
import com.taskpro.backend.entity.User;
import com.taskpro.backend.enums.CollaboratorRoleEnum;
import com.taskpro.backend.repository.ProjectCollaboratorRepository;
import com.taskpro.backend.repository.ProjectRepository;
import com.taskpro.backend.repository.UserRepository;
import com.taskpro.backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectCollaboratorRepository projectCollaboratorRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProjectDto createProject(ProjectRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Project project = new Project();
        project.setName(request.getName());
        Project savedProject = projectRepository.save(project);
        ProjectCollaborator collaborator = new ProjectCollaborator();
        collaborator.setProject(savedProject);
        collaborator.setUser(user);
        collaborator.setRole(CollaboratorRoleEnum.OWNER);
        projectCollaboratorRepository.save(collaborator);
        return new ProjectDto(savedProject);
    }

    public ProjectDto getProject(Long projectId) {
        Long userId = SecurityUtil.getCurrentUserId();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        boolean isCollaborator = project.getCollaborators().stream()
                .anyMatch(collaborator -> collaborator.getUser().getId().equals(userId));
        if (!isCollaborator) {
            throw new SecurityException("You are not authorized to view this project.");
        }
        return new ProjectDto(project);
    }

    public List<ProjectSummaryDto> getAllProjectOfUser() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<Project> projects = projectRepository.findAllByCollaboratorUserId(userId);

        return projects.stream()
                .map(ProjectSummaryDto::new)
                .toList();
    }

    @Transactional
    public ProjectDto updateProject(Long projectId, ProjectRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        boolean isOwner = project.getCollaborators().stream()
                .anyMatch(collaborator -> collaborator.getUser().getId().equals(userId) && collaborator.getRole().equals(CollaboratorRoleEnum.OWNER));

        if (!isOwner) {
            throw new SecurityException("You are not authorized to update this project.");
        }

        project.setName(request.getName());
        projectRepository.save(project);

        return new ProjectDto(project);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        Long userId = SecurityUtil.getCurrentUserId();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        boolean isOwner = project.getCollaborators().stream()
                .anyMatch(collaborator -> collaborator.getUser().getId().equals(userId) && collaborator.getRole().equals(CollaboratorRoleEnum.OWNER));

        if (!isOwner) {
            throw new SecurityException("You are not authorized to delete this project.");
        }

        projectRepository.delete(project);
    }

    @Transactional
    public ProjectCollaboratorDto createProjectCollaborator(Long projectId, ProjectCollaboratorRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        User collaboratorUser = userRepository.findByEmail(request.getUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        boolean isOwner = project.getCollaborators().stream()
                .anyMatch(collaborator -> collaborator.getUser().getId().equals(userId) && collaborator.getRole().equals(CollaboratorRoleEnum.OWNER));

        if (!isOwner) {
            throw new SecurityException("You are not authorized to add collaborators");
        }
        if (!EnumSet.of(CollaboratorRoleEnum.EDITOR, CollaboratorRoleEnum.VIEWER).contains(request.getRole())) {
            throw new IllegalArgumentException("Invalid role. Only 'editor' or 'viewer' roles can be assigned.");
        }

        ProjectCollaborator projectCollaborator = new ProjectCollaborator();
        projectCollaborator.setProject(project);
        projectCollaborator.setUser(collaboratorUser);
        projectCollaborator.setRole(request.getRole());

        return new ProjectCollaboratorDto(projectCollaboratorRepository.save(projectCollaborator));
    }

    @Transactional
    public void deleteCollaborator(Long projectId, Long collaboratorUserId) {
        Long userId = SecurityUtil.getCurrentUserId();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find project"));

        boolean isOwner = project.getCollaborators().stream()
                .anyMatch(collaborator -> collaborator.getUser().getId().equals(userId) && collaborator.getRole().equals(CollaboratorRoleEnum.OWNER));

        if (!isOwner) {
            throw new SecurityException("You are not authorized to remove collaborators");
        }
        if (userId.equals(collaboratorUserId)) {
            throw new IllegalArgumentException("Cannot remove owner itself");
        }

        ProjectCollaborator collaborator = projectCollaboratorRepository.findByProjectIdAndUserId(projectId, collaboratorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Collaborator not found for this task"));

        projectCollaboratorRepository.delete(collaborator);
    }
}
