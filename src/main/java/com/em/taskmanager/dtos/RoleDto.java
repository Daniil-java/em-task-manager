package com.em.taskmanager.dtos;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class RoleDto {
    private String name;
}
