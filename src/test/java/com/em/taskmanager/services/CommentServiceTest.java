package com.em.taskmanager.services;

import com.em.taskmanager.dtos.CommentDto;
import com.em.taskmanager.dtos.mappers.CommentMapper;
import com.em.taskmanager.entities.task.Comment;
import com.em.taskmanager.repositories.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class CommentServiceTest {
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TaskService taskService;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setContent("Test comment");
    }

    @Test
    void testGetCommentsByTaskId() {
        Long taskId = 1L;
        Page<Comment> commentsPage = new PageImpl<>(Collections.singletonList(comment));
        doNothing().when(taskService).checkTaskExistsById(taskId);
        when(commentRepository.findCommentsByTaskId(eq(taskId), any(Pageable.class)))
                .thenReturn(commentsPage);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        Page<CommentDto> result = commentService.getCommentsByTaskId(taskId, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(commentDto, result.getContent().get(0));
    }

    @Test
    void testGetCommentById() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        CommentDto result = commentService.getCommentById(1L);

        assertNotNull(result);
        assertEquals(commentDto, result);
    }

    @Test
    void testAddComment() {
        Long taskId = 1L;
        doNothing().when(taskService).checkTaskExistsById(taskId);
        when(commentMapper.toEntity(commentDto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        commentDto.setId(null);
        CommentDto result = commentService.addComment(taskId, commentDto);

        assertNotNull(result);
        assertEquals(commentDto, result);
    }

    @Test
    void testRemoveCommentById() {
        when(commentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(commentRepository).deleteById(1L);

        assertDoesNotThrow(() -> commentService.removeCommentById(1L));
    }
}
