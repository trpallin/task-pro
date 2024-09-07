package com.taskpro.backend.service;

import com.taskpro.backend.dto.CreateTaskRequest;
import com.taskpro.backend.entity.Task;
import com.taskpro.backend.entity.User;
import com.taskpro.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task createTask(User user, CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setCreatedBy(user);
        if (request.getParentTaskId() != null) {
            Task parentTask = taskRepository.findById(request.getParentTaskId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent task not found"));
            if (!parentTask.getCreatedBy().getId().equals(user.getId())) {
                throw new SecurityException("You are not authorized to create a subtask for this task");
            }
            task.setParentTask(parentTask);
        }
        return taskRepository.save(task);
    }

    public Task getTask(Long taskId, Long userId) {
        return taskRepository.findById(taskId)
                .filter(task -> task.getCreatedBy().getId().equals(userId) && task.getDeletedAt() == null)
                .orElseThrow(() -> new SecurityException("Unauthorized or task not found"));
    }

    public Task updateTask(Long taskId, Long userId, CreateTaskRequest request) {
        Task existingTask = taskRepository.findById(taskId)
                .filter(task -> task.getDeletedAt() == null)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (!existingTask.getCreatedBy().getId().equals(userId)) {
            throw new SecurityException("Not allowed to update this task");
        }

        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setStatus(request.getStatus());
        existingTask.setPriority(request.getPriority());
        existingTask.setDueDate(request.getDueDate());

        return taskRepository.save(existingTask);
    }

    public void softDeleteTask(Long taskId, Long userId) {
        Task task = taskRepository.findByIdAndCreatedBy_Id(taskId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found or you are not authorized"));
        if (task.getDeletedAt() == null) {
            softDeleteTaskRecursively(task, LocalDateTime.now());
            taskRepository.save(task);
        }
    }

    private void softDeleteTaskRecursively(Task task, LocalDateTime deletionTime) {
        task.setDeletedAt(deletionTime);

        if (task.getSubtasks() != null && !task.getSubtasks().isEmpty()) {
            for (Task subtask : task.getSubtasks()) {
                if (subtask.getDeletedAt() == null) {
                    softDeleteTaskRecursively(subtask, deletionTime);
                }
            }
        }
    }

    public List<Task> getTopTasksByUserId(Long userId) {
        return taskRepository.findAllByCreatedBy_Id(userId)
                .stream()
                .filter(task -> task.getParentTask() == null && task.getDeletedAt() == null)
                .toList();
    }
}
