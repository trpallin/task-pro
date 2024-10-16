package com.taskpro.backend.dto;

import com.taskpro.backend.enums.TaskPriorityEnum;
import com.taskpro.backend.enums.TaskStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {
    @NotNull
    private Long projectId;
    @NotNull
    private String title;
    private String description;
    @NotNull
    private TaskStatusEnum status;
    @NotNull
    private TaskPriorityEnum priority;
    private LocalDate dueDate;
    private Long parentTaskId;
}
