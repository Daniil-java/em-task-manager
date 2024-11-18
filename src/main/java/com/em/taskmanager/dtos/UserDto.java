package com.em.taskmanager.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;


@Data
@Accessors(chain = true)
public class UserDto {
    private Long id;
    private String username;
    private Set<RoleDto> roles;
}
