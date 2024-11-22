package com.em.taskmanager.mappers;

import com.em.taskmanager.dtos.CommentDto;
import com.em.taskmanager.dtos.UserDto;
import com.em.taskmanager.dtos.mappers.CommentMapper;
import com.em.taskmanager.entities.User;
import com.em.taskmanager.entities.task.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CommentMapperTest {
    @Autowired
    private CommentMapper commentMapper;

    @Test
    @DisplayName("Конвертация сущности в DTO")
    void testToDto() {
        Comment comment = new Comment()
                .setId(1L)
                .setContent("Test content")
                .setAuthor(new User().setId(1L).setUsername("name"))
                .setCreated(OffsetDateTime.now());

        CommentDto commentDto = commentMapper.toDto(comment);

        assertNotNull(commentDto);
        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(comment.getAuthor().getId(), commentDto.getAuthor().getId());
        assertEquals(comment.getAuthor().getUsername(), commentDto.getAuthor().getUsername());
        assertEquals(comment.getContent(), commentDto.getContent());
    }

    @Test
    @DisplayName("Конвертация ДТО в сущность")
    void testToEntity() {
        CommentDto commentDto = new CommentDto()
                .setId(1L)
                .setContent("Test content")
                .setAuthor(new UserDto().setId(1L).setUsername("name"))
                .setCreated(OffsetDateTime.now());

        Comment comment = commentMapper.toEntity(commentDto);

        assertNotNull(comment);
        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(comment.getAuthor().getId(), commentDto.getAuthor().getId());
        assertEquals(comment.getAuthor().getUsername(), commentDto.getAuthor().getUsername());
        assertEquals(comment.getContent(), commentDto.getContent());
    }

    @Test
    @DisplayName("Конвертация списка сущностей в список DTO")
    void testToDtoList() {
        List<Comment> commentList = Arrays.asList(new Comment(), new Comment());
        List<CommentDto> commentDtoList = commentMapper.toDtoList(commentList);

        assertNotNull(commentDtoList);
        assertEquals(commentList.size(), commentDtoList.size());
    }

    @Test
    @DisplayName("Конвертация списка дто в список сущностей")
    void testToEntityList() {
        List<CommentDto> commentDtoList = Arrays.asList(new CommentDto(), new CommentDto());
        List<Comment> commentList = commentMapper.toEntityList(commentDtoList);

        assertNotNull(commentList);
        assertEquals(commentDtoList.size(), commentList.size());
    }
}

