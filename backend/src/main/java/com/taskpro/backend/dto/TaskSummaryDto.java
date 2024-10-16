package com.taskpro.backend.dto;

import com.taskpro.backend.entity.Task;
import com.taskpro.backend.enums.TaskStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskSummaryDto {
    private Long id;
    private String title;
    private TaskStatusEnum status;
    private LocalDateTime createdAt;

    public TaskSummaryDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.status = task.getStatus();
        this.createdAt = task.getCreatedAt();
    }
}
