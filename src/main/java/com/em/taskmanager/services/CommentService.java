package com.em.taskmanager.services;

import com.em.taskmanager.dtos.CommentDto;
import com.em.taskmanager.dtos.mappers.CommentMapper;
import com.em.taskmanager.entities.task.Comment;
import com.em.taskmanager.exceptions.ErrorResponseException;
import com.em.taskmanager.exceptions.ErrorStatus;
import com.em.taskmanager.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final TaskService taskService;

    public Page<CommentDto> getCommentsByTaskId(Long taskId, int page, int pageSize) {
        taskService.checkTaskExistsById(taskId);

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Comment> commentPage = commentRepository.findCommentsByTaskId(taskId, pageable);

        return commentPage.map(commentMapper::toDto);
    }

    public CommentDto getCommentById(Long id) {
        return commentMapper.toDto(commentRepository.findById(id)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.COMMENT_NOT_FOUND)));
    }

    public CommentDto addComment(Long taskId, CommentDto commentDto) {
        if (commentDto.getId() != null) {
            throw new ErrorResponseException(ErrorStatus.COMMENT_CREATION_ERROR);
        }

        taskService.checkTaskExistsById(taskId);

        return commentMapper.toDto(
                commentRepository.save(commentMapper.toEntity(commentDto)));
    }

    public void removeCommentById(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
        } else {
            throw new ErrorResponseException(ErrorStatus.COMMENT_NOT_FOUND);
        }
    }
}
