package com.taskpro.backend.service;

import com.taskpro.backend.dto.CreateTaskRequest;
import com.taskpro.backend.entity.Task;
import com.taskpro.backend.entity.User;
import com.taskpro.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Task getTaskByIdAndUserId(Long taskId, Long userId) {
        return taskRepository.findById(taskId)
                .filter(task -> task.getCreatedBy().getId().equals(userId))
                .orElseThrow(() -> new SecurityException("Unauthorized or task not found"));
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findAllByCreatedBy_Id(userId);
    }
}
