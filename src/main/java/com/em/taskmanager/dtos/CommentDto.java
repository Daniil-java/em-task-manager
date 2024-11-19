package com.em.taskmanager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Data
@Accessors(chain = true)
@Schema(description = "Модель комментария")
public class CommentDto {
    @Schema(description = "Идентификатор комментария")
    private Long id;
    @NotNull
    @Schema(description = "Содержание комментария")
    private String content;
    @Schema(description = "Автор комментария")
    private UserDto author;
    @Schema(description = "Дата создания комментария")
    private OffsetDateTime created;
}
