package com.em.taskmanager.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CommentDto {
    private Long id;
    private String content;
    private UserDto author;
    private LocalDateTime created;
}
