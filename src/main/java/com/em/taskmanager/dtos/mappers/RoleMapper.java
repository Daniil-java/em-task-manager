package com.em.taskmanager.dtos.mappers;

import com.em.taskmanager.dtos.RoleDto;
import com.em.taskmanager.entities.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);

    List<RoleDto> toDtoList(List<Role> roleList);

    List<Role> toEntityList(List<RoleDto> roleDtoList);
}
