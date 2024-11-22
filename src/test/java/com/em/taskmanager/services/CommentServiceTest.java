package com.em.taskmanager.services;

import com.em.taskmanager.dtos.CommentDto;
import com.em.taskmanager.dtos.CustomUserDetails;
import com.em.taskmanager.dtos.UserDto;
import com.em.taskmanager.dtos.mappers.CommentMapper;
import com.em.taskmanager.entities.Role;
import com.em.taskmanager.entities.RoleName;
import com.em.taskmanager.entities.User;
import com.em.taskmanager.entities.task.Comment;
import com.em.taskmanager.entities.task.Task;
import com.em.taskmanager.exceptions.ErrorResponseException;
import com.em.taskmanager.exceptions.ErrorStatus;
import com.em.taskmanager.repositories.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

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
    @Mock
    private UserService userService;

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
        comment.setAuthor(new User().setId(1L));

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setContent("Test comment");
        commentDto.setAuthor(new UserDto().setId(1L));;
    }

    @Test
    @DisplayName("Получение списка комментариев по задаче: корректные данные")
    void testGetCommentsByTaskId() {
        Long taskId = 1L;
        User user = new User()
                .setId(1L)
                .setRoles(Set.of(new Role().setName(RoleName.ROLE_USER)));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(CustomUserDetails.toUserDetails(user));

        Page<Comment> commentsPage = new PageImpl<>(Collections.singletonList(comment));
        doNothing().when(taskService).checkTaskExistsById(taskId);
        when(taskService.getTaskById(taskId)).thenReturn(new Task().setAssignee(user));
        when(commentRepository.findCommentsByTaskId(eq(taskId), any(Pageable.class)))
                .thenReturn(commentsPage);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        assertDoesNotThrow(() -> commentService.getCommentsByTaskId(authentication, taskId, 0, 5));
    }

    @Test
    @DisplayName("Получение списка комментариев по задаче: access denied")
    void testGetCommentsByTaskIdThrowsAccessDenied() {
        Long taskId = 1L;
        User user = new User()
                .setId(1L)
                .setRoles(Set.of(new Role().setName(RoleName.ROLE_USER)));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(CustomUserDetails.toUserDetails(user));

        doNothing().when(taskService).checkTaskExistsById(taskId);
        when(taskService.getTaskById(taskId)).thenReturn(new Task().setAssignee(new User().setId(2L)));

        ErrorResponseException errorResponseException = assertThrows(ErrorResponseException.class,
                () ->  commentService.getCommentsByTaskId(authentication, taskId, 0 , 5));

        assertEquals(ErrorStatus.COMMENT_ASSIGNEE_ACCESS_DENIED, errorResponseException.getErrorStatus());
    }

    @Test
    @DisplayName("Получение комментария по ид: not found")
    void testGetCommentById() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        Long commentId = comment.getId();
        ErrorResponseException errorResponseException = assertThrows(ErrorResponseException.class,
                () ->  commentService.getCommentById(commentId));

        assertEquals(ErrorStatus.COMMENT_NOT_FOUND, errorResponseException.getErrorStatus());
    }

    @Test
    @DisplayName("Добавление нового комментария: invalid variable")
    void testAddCommentThrowInvalidVariables() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(CustomUserDetails.toUserDetails(new User()
                .setId(1L)
                .setRoles(Set.of(new Role().setName(RoleName.ROLE_ADMIN)))
        ));

        Task correctTask = new Task()
                .setAssignee(new User().setId(1L));
        Long taskId = 1L;
        when(taskService.getTaskById(taskId)).thenReturn(correctTask);

        ErrorResponseException errorResponseException = assertThrows(ErrorResponseException.class,
                () ->  commentService.addComment(authentication, taskId, commentDto));

        assertEquals(ErrorStatus.COMMENT_CREATION_ERROR, errorResponseException.getErrorStatus());
    }

    @Test
    @DisplayName("Добавление нового комментария: access denied")
    void testAddCommentThrowAccessDenied() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(CustomUserDetails.toUserDetails(new User()
                .setId(2L)
                .setRoles(Set.of(new Role().setName(RoleName.ROLE_USER)))
        ));

        Task correctTask = new Task()
                .setAssignee(new User().setId(1L));
        Long taskId = 1L;
        when(taskService.getTaskById(taskId)).thenReturn(correctTask);

        commentDto.setId(null);
        ErrorResponseException errorResponseException = assertThrows(ErrorResponseException.class,
                () ->  commentService.addComment(authentication, taskId, commentDto));

        assertEquals(ErrorStatus.COMMENT_ACCESS_DENIED, errorResponseException.getErrorStatus());

    }

    @Test
    @DisplayName("Добавление нового комментария: корректные данные")
    void testAddComment() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(CustomUserDetails.toUserDetails(new User()
                .setId(1L)
                .setRoles(Set.of(new Role().setName(RoleName.ROLE_USER)))
        ));

        Task correctTask = new Task()
                .setAssignee(new User().setId(1L));

        Long taskId = 1L;

        when(userService.getUserById(1L)).thenReturn(new User().setId(1L));
        when(taskService.getTaskById(taskId)).thenReturn(correctTask);
        when(commentMapper.toEntity(commentDto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        commentDto.setId(null);
        CommentDto result = commentService.addComment(authentication, taskId, commentDto);

        assertDoesNotThrow(() -> commentService.addComment(authentication, taskId, commentDto));
        assertNotNull(result);
        assertEquals(commentDto, result);
    }
}
