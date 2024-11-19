package com.em.taskmanager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@Schema(description = "Модель роли")
public class RoleDto {
    @NotNull
    @Schema(description = "Название роли")
    private String name;
}
