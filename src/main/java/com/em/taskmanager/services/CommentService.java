package com.em.taskmanager.services;

import com.em.taskmanager.dtos.CommentDto;
import com.em.taskmanager.dtos.CustomUserDetails;
import com.em.taskmanager.dtos.mappers.CommentMapper;
import com.em.taskmanager.entities.RoleName;
import com.em.taskmanager.entities.User;
import com.em.taskmanager.entities.task.Comment;
import com.em.taskmanager.entities.task.Task;
import com.em.taskmanager.exceptions.ErrorResponseException;
import com.em.taskmanager.exceptions.ErrorStatus;
import com.em.taskmanager.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;

    public Page<CommentDto> getCommentsByTaskId(Authentication authentication, Long taskId, int page, int pageSize) {
        taskService.checkTaskExistsById(taskId);

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        if (!user.hasRole(RoleName.ROLE_ADMIN)) {
            Task task = taskService.getTaskById(taskId);
            if (!task.getAssignee().getId().equals(user.getId())) {
                throw new ErrorResponseException(ErrorStatus.COMMENT_ASSIGNEE_ACCESS_DENIED);
            }
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Comment> commentPage = commentRepository.findCommentsByTaskId(taskId, pageable);

        return commentPage.map(commentMapper::toDto);
    }

    public CommentDto getCommentById(Long id) {
        return commentMapper.toDto(commentRepository.findById(id)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.COMMENT_NOT_FOUND)));
    }

    public CommentDto addComment(Authentication authentication, Long taskId, CommentDto commentDto) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Task task = taskService.getTaskById(taskId);

        if (!user.hasRole(RoleName.ROLE_ADMIN) &&
                (task.getAssignee() == null || !user.getId().equals(task.getAssignee().getId()))) {
            throw new ErrorResponseException(ErrorStatus.COMMENT_ACCESS_DENIED);
        }

        if (commentDto.getId() != null) {
            throw new ErrorResponseException(ErrorStatus.COMMENT_CREATION_ERROR);
        }

        User author = userService.getUserById(user.getId());

        return commentMapper.toDto(
                commentRepository.save(commentMapper.toEntity(commentDto)
                        .setTask(task)
                        .setAuthor(author)
                )
        );
    }

    public void removeCommentById(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
        } else {
            throw new ErrorResponseException(ErrorStatus.COMMENT_NOT_FOUND);
        }
    }
}
