package com.taskpro.backend.controller;

import com.taskpro.backend.dto.CreateTaskRequest;
import com.taskpro.backend.dto.TaskDto;
import com.taskpro.backend.entity.Task;
import com.taskpro.backend.service.TaskService;
import com.taskpro.backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskRequest request) {
        TaskDto response = new TaskDto(taskService.createTask(request));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long taskId) {
        Task task = taskService.getTask(taskId);
        TaskDto taskDto = new TaskDto(task);
        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long taskId, @RequestBody CreateTaskRequest request) {
        Task task = taskService.updateTask(taskId, request);
        TaskDto taskDto = new TaskDto(task);
        return ResponseEntity.ok(taskDto);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.softDeleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getTasksOfUser() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<Task> tasks = taskService.getTopTasksByUserId(userId);
        List<TaskDto> taskDtos = tasks.stream()
                .map(TaskDto::new)
                .toList();
        return ResponseEntity.ok(taskDtos);
    }
}
