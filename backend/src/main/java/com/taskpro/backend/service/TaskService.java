package com.taskpro.backend.service;

import com.taskpro.backend.dto.TaskRequest;
import com.taskpro.backend.dto.TaskDto;
import com.taskpro.backend.entity.Project;
import com.taskpro.backend.entity.Task;
import com.taskpro.backend.entity.User;
import com.taskpro.backend.repository.ProjectRepository;
import com.taskpro.backend.repository.TaskRepository;
import com.taskpro.backend.repository.UserRepository;
import com.taskpro.backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskDto createTask(TaskRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean hasPermission = project.getCollaborators().stream()
                .anyMatch(collaborator -> collaborator.getUser().getId().equals(userId) &&
                        collaborator.getRole().hasEditPermission());

        if (!hasPermission) {
            throw new SecurityException("You are not authorized to create task.");
        }

        Task task = new Task();
        task.setProject(project);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setCreatorUser(user);

        if (request.getParentTaskId() != null) {
            Task parentTask = taskRepository.findByIdWithLock(request.getParentTaskId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent task not found"));
            task.setParentTask(parentTask);
        }

        return new TaskDto(taskRepository.save(task));
    }

    public TaskDto getTask(Long taskId) {
        Long userId = SecurityUtil.getCurrentUserId();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (task.getProject() == null) {
            throw new IllegalArgumentException("Project not found");
        }

        boolean hasPermission = task.getProject().getCollaborators().stream()
                .anyMatch(collaborator -> collaborator.getUser().getId().equals(userId) &&
                        collaborator.getRole().hasReadPermission());

        if (!hasPermission) {
            throw new SecurityException("You are not authorized to create task.");
        }

        return new TaskDto(task);
    }

    @Transactional
    public TaskDto updateTask(Long taskId, TaskRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        Task existingTask = taskRepository.findByIdWithLock(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        boolean hasPermission = existingTask.getProject().getCollaborators().stream()
                .anyMatch(collaborator -> collaborator.getUser().getId().equals(userId) &&
                        collaborator.getRole().hasEditPermission());

        if (!hasPermission) {
            throw new SecurityException("You are not authorized to create task.");
        }

        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setStatus(request.getStatus());
        existingTask.setPriority(request.getPriority());
        existingTask.setDueDate(request.getDueDate());

        return new TaskDto(taskRepository.save(existingTask));
    }

    @Transactional
    public void deleteTask(Long taskId) {
        Long userId = SecurityUtil.getCurrentUserId();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        boolean hasPermission = task.getProject().getCollaborators().stream()
                .anyMatch(collaborator -> collaborator.getUser().getId().equals(userId) &&
                        collaborator.getRole().hasEditPermission());

        if (!hasPermission) {
            throw new SecurityException("You are not authorized to delete task.");
        }

        taskRepository.delete(task);
    }

    public List<TaskDto> getRootTasksByProjectId(Long projectId) {
        Long userId = SecurityUtil.getCurrentUserId();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        boolean hasPermission = project.getCollaborators().stream()
                .anyMatch(collaborator -> collaborator.getUser().getId().equals(userId) && collaborator.getRole().hasReadPermission());

        if (!hasPermission) {
            throw new SecurityException("You are not authorized");
        }

        return project.getTasks().stream()
                .filter(task -> task.getParentTask() == null)
                .map(TaskDto::new)
                .toList();
    }
}
