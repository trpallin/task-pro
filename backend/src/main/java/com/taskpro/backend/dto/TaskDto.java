package com.taskpro.backend.dto;

import com.taskpro.backend.entity.Task;
import com.taskpro.backend.enums.TaskPriorityEnum;
import com.taskpro.backend.enums.TaskStatusEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatusEnum status;
    private TaskPriorityEnum priority;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long parentTaskId;
    private List<TaskDto> subtasks;

    public TaskDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.priority = task.getPriority();
        this.dueDate = task.getDueDate();
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();

        if (task.getParentTask() != null) {
            this.parentTaskId = task.getParentTask().getId();
        } else {
            this.parentTaskId = null;
        }

        if (task.getSubtasks() != null) {
            this.subtasks = task.getSubtasks().stream()
                    .map(TaskDto::new)
                    .toList();
        }
    }
}
