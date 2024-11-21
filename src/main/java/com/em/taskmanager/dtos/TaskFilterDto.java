package com.em.taskmanager.dtos;

import com.em.taskmanager.entities.task.TaskPriority;
import com.em.taskmanager.entities.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Schema(description = "Модель фильтра задач")
public class TaskFilterDto {
    @Schema(description = "Идентификатор автора задачи")
    private Long authorId;
    @Schema(description = "Идентификатор исполнителя задачи")
    private Long assigneeId;
    @Schema(description = "Текущий статус задачи")
    private TaskStatus status;
    @Schema(description = "Текущий приоритет задачи")
    private TaskPriority priority;
    @Schema(description = "Данные для поиска по заголовку и описанию задачи")
    private String searchQuery;
    @Schema(description = "Задача создана после")
    private OffsetDateTime createdFrom;
    @Schema(description = "Задача создана до")
    private OffsetDateTime createdTo;
    @Schema(description = "Задача обновлялась от")
    private OffsetDateTime updatedFrom;
    @Schema(description = "Задача обновлялась до")
    private OffsetDateTime updatedTo;
    @Schema(description = "Номер страницы", defaultValue = "0")
    private int page = 0;
    @Schema(description = "Количество элементов на странице", defaultValue = "10")
    private int pageSize = 10;
}
