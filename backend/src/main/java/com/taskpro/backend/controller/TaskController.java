package com.taskpro.backend.controller;

import com.taskpro.backend.dto.CreateTaskRequest;
import com.taskpro.backend.dto.TaskDto;
import com.taskpro.backend.entity.Task;
import com.taskpro.backend.entity.User;
import com.taskpro.backend.service.TaskService;
import com.taskpro.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskRequest request) {
        User user = userService.getCurrentUser();
        TaskDto response = new TaskDto(taskService.createTask(user, request));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long taskId) {
        User user = userService.getCurrentUser();
        Task task = taskService.getTask(taskId, user.getId());
        TaskDto taskDto = new TaskDto(task);
        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long taskId, @RequestBody CreateTaskRequest request) {
        User user = userService.getCurrentUser();
        Task task = taskService.updateTask(taskId, user.getId(), request);
        TaskDto taskDto = new TaskDto(task);
        return ResponseEntity.ok(taskDto);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        User user = userService.getCurrentUser();
        taskService.softDeleteTask(taskId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getTasksOfUser() {
        User user = userService.getCurrentUser();
        List<Task> tasks = taskService.getTopTasksByUserId(user.getId());
        List<TaskDto> taskDtos = tasks.stream()
                .map(TaskDto::new)
                .toList();
        return ResponseEntity.ok(taskDtos);
    }
}
