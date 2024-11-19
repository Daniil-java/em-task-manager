package com.em.taskmanager.dtos;

import com.em.taskmanager.entities.task.TaskPriority;
import com.em.taskmanager.entities.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;


@Data
@Accessors(chain = true)
@Schema(description = "Модель задачи")
public class TaskDto {
    @Schema(description = "Идентификатор задачи")
    private Long id;
    @NotNull
    @Schema(description = "Название задачи")
    private String title;
    @Schema(description = "Описание задачи")
    private String description;
    @NotNull
    @Schema(description = "Стутус задачи")
    private TaskStatus status;
    @Schema(description = "Приоритет задачи")
    private TaskPriority taskPriority;
    @Schema(description = "Автор задачи")
    private UserDto author;
    @Schema(description = "Исполнитель задачи")
    private UserDto assignee;
    @Schema(description = "Дата последнего обновления задачи")
    private OffsetDateTime updated;
    @Schema(description = "Дата создания задачи")
    private OffsetDateTime created;

}
