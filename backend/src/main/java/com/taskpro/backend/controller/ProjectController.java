package com.taskpro.backend.controller;

import com.taskpro.backend.dto.*;
import com.taskpro.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProject(projectId));
    }

    @GetMapping
    public ResponseEntity<List<ProjectSummaryDto>> getAllProject() {
        return ResponseEntity.ok(projectService.getAllProjectOfUser());
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<Void> updateProject(@PathVariable Long projectId, @RequestBody ProjectRequest request) {
        projectService.updateProject(projectId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/collaborators")
    public ResponseEntity<ProjectCollaboratorDto> createCollaborator(@PathVariable Long projectId, @RequestBody ProjectCollaboratorRequest request) {
        return ResponseEntity.ok(projectService.createProjectCollaborator(projectId, request));
    }

    @DeleteMapping("/{projectId}/collaborators/{collaboratorId}")
    public ResponseEntity<Void> deleteCollaborator(@PathVariable Long projectId, @PathVariable Long collaboratorId) {
        projectService.deleteCollaborator(projectId, collaboratorId);
        return ResponseEntity.noContent().build();
    }
}
