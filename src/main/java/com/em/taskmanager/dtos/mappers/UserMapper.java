package com.em.taskmanager.dtos.mappers;

import com.em.taskmanager.dtos.UserDto;
import com.em.taskmanager.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User userEntity);
    UserDto toEntity(UserDto userDto);
}
