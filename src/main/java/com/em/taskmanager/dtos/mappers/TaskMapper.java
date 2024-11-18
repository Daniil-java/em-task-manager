package com.em.taskmanager.dtos.mappers;


import com.em.taskmanager.dtos.TaskDto;
import com.em.taskmanager.entities.task.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public interface TaskMapper {
    TaskDto toDto(Task task);
    Task toEntity(TaskDto taskDto);
    List<TaskDto> toDtoList(List<Task> taskList);
    List<Task> toEntityList(List<TaskDto> taskDtoList);
}
