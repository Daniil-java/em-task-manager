package com.em.taskmanager.dtos.mappers;

import com.em.taskmanager.dtos.CommentDto;
import com.em.taskmanager.entities.task.Comment;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {
    CommentDto toDto(Comment comment);
    Comment toEntity(CommentDto commentDto);
    List<CommentDto> toDtoList(List<Comment> commentList);
    List<Comment> toEntityList(List<CommentDto> commentDtoList);
}
