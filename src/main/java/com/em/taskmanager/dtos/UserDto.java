package com.em.taskmanager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;


@Data
@Accessors(chain = true)
@Schema(description = "Модель пользователя")
public class UserDto {
    @Schema(description = "Идентификатор пользователя")
    private Long id;
    @Schema(description = "Имя пользователя")
    private String username;
    @Schema(description = "Роли пользователя")
    private Set<RoleDto> roles;
}
