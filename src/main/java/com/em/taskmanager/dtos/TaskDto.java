package com.em.taskmanager.dtos;

import com.em.taskmanager.entities.task.TaskPriority;
import com.em.taskmanager.entities.task.TaskStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;


@Data
@Accessors(chain = true)
public class TaskDto {
    private Long id;
    private String tittle;
    private String description;
    private TaskStatus status;
    private TaskPriority taskPriority;
    private UserDto author;
    private UserDto assignee;
    private OffsetDateTime updated;
    private OffsetDateTime created;

}
