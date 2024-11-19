package com.em.taskmanager.dtos;

import com.em.taskmanager.entities.task.TaskPriority;
import com.em.taskmanager.entities.task.TaskStatus;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TaskFilterDto {
    private Long authorId;
    private Long assignee;
    private TaskStatus status;
    private TaskPriority priority;
    private String tittle;
    private OffsetDateTime createdFrom;
    private OffsetDateTime createdTo;
    private OffsetDateTime updatedFrom;
    private OffsetDateTime updatedTo;
    private int page = 0;
    private int pageSize = 10;
}
