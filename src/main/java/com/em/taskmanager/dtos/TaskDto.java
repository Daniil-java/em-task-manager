package com.em.taskmanager.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Accessors(chain = true)
public class TaskDto {
    private Long id;
    private String tittle;
    private String description;
    private List<CommentDto> comments;
    private String status;
    private String taskPriority;
    private UserDto author;
    private UserDto assignee;
    private LocalDateTime updated;
    private LocalDateTime created;

}
