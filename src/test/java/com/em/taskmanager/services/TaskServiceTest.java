package com.em.taskmanager.services;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.em.taskmanager.dtos.CustomUserDetails;
import com.em.taskmanager.dtos.TaskDto;
import com.em.taskmanager.dtos.TaskFilterDto;
import com.em.taskmanager.dtos.mappers.TaskMapper;
import com.em.taskmanager.entities.Role;
import com.em.taskmanager.entities.RoleName;
import com.em.taskmanager.entities.User;
import com.em.taskmanager.entities.task.Task;
import com.em.taskmanager.entities.task.TaskStatus;
import com.em.taskmanager.exceptions.ErrorResponseException;
import com.em.taskmanager.exceptions.ErrorStatus;
import com.em.taskmanager.repositories.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserService userService;

    @Mock
    private TaskMapper taskMapper;

    private TaskFilterDto taskFilterDto;
    private TaskDto taskDto;
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskFilterDto = new TaskFilterDto();
        taskDto = new TaskDto();
        taskDto.setId(1L);
        task = new Task();
        task.setId(1L);
    }

    @Test
    @DisplayName("Получени задачи по ид: корректные данные")
    void testGetTaskDtoById() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        assertDoesNotThrow(() -> taskService.getTaskDtoById(1L));
        verify(taskRepository, times(1)).findById(task.getId());
    }

    @Test
    @DisplayName("Добавление новой задачи: корректные данные")
    void testAddTask() {
        taskDto.setId(null);

        User user = new User()
                .setId(1L)
                .setRoles(Set.of(new Role().setName(RoleName.ROLE_USER)));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(CustomUserDetails.toUserDetails(user));

        when(userService.getUserById(user.getId())).thenReturn(user);
        when(taskMapper.toEntity(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        assertDoesNotThrow(() -> taskService.addTask(authentication, taskDto));
    }

    @Test
    @DisplayName("Добавление новой задачи: invalid variables")
    void testAddTaskThrowsCreationError() {
        Authentication authentication = Mockito.mock(Authentication.class);

        ErrorResponseException errorResponseException = assertThrows(ErrorResponseException.class,
                () ->  taskService.addTask(authentication, taskDto));
        Assertions.assertEquals(ErrorStatus.TASK_CREATION_ERROR, errorResponseException.getErrorStatus());
    }

    @Test
    @DisplayName("Редактироваание задачи: invalid variables")
    void testEditTaskThrowsUpdateError() {
        taskDto.setId(taskDto.getId() + 1L);

        Long taskId = task.getId();
        ErrorResponseException errorResponseException = assertThrows(ErrorResponseException.class,
                () ->  taskService.editTask(taskId, taskDto));
        assertEquals(ErrorStatus.TASK_UPDATE_ERROR, errorResponseException.getErrorStatus());
    }

    @Test
    @DisplayName("Удаление задачи: корректные данные")
    void testRemoveTaskById() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> taskService.removeTaskById(1L));
        verify(taskRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Получение задачи по ид: not found")
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        ErrorResponseException errorResponseException = assertThrows(ErrorResponseException.class,
                () ->  taskService.getTaskDtoById(1L));
        assertEquals(ErrorStatus.TASK_NOT_FOUND, errorResponseException.getErrorStatus());
    }

    @Test
    @DisplayName("Получение списка задач: корректные данные")
    void testGetTasks() {
        User user = new User()
                .setId(1L)
                .setRoles(Set.of(new Role().setName(RoleName.ROLE_USER)));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(CustomUserDetails.toUserDetails(user));

        taskFilterDto.setAssigneeId(user.getId());

        PageRequest pageRequest = PageRequest.of(taskFilterDto.getPage(), taskFilterDto.getPageSize());
        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));

        when(taskRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(taskPage);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        assertDoesNotThrow(() -> taskService.getTasks(authentication, taskFilterDto));
    }

    @Test
    @DisplayName("Получение списка задача: access denied")
    void testGetTasksThrowsAccessDenied() {
        User user = new User()
                .setId(1L)
                .setRoles(Set.of(new Role().setName(RoleName.ROLE_USER)));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(CustomUserDetails.toUserDetails(user));

        ErrorResponseException errorResponseException = assertThrows(ErrorResponseException.class,
                () ->  taskService.getTasks(authentication, taskFilterDto));

        assertEquals(ErrorStatus.TASK_ASSIGNEE_ACCESS_DENIED, errorResponseException.getErrorStatus());
    }

    @Test
    @DisplayName("Обновление статуса задачи: корректные данные")
    void testUpdateTaskStatus() {
        User user = new User()
                .setId(1L)
                .setRoles(Set.of(new Role().setName(RoleName.ROLE_USER)));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(CustomUserDetails.toUserDetails(user));

        task.setAssignee(user);

        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        assertDoesNotThrow(() -> taskService.updateTaskStatus(authentication, task.getId(), TaskStatus.PENDING.name()));
    }

    @Test
    @DisplayName("Обновление статуса задачи: forbidden")
    void testUpdateTaskStatusThrowsAssigneeNotFound() {
        User user = new User()
                .setId(1L)
                .setRoles(Set.of(new Role().setName(RoleName.ROLE_USER)));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(CustomUserDetails.toUserDetails(user));


        Long taskId = task.getId();
        String status = TaskStatus.PENDING.name();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        ErrorResponseException errorResponseException = assertThrows(ErrorResponseException.class,
                () -> taskService.updateTaskStatus(authentication, taskId, status));

        assertEquals(ErrorStatus.TASK_ASSIGNEE_ACCESS_DENIED, errorResponseException.getErrorStatus());
    }

    @Test
    @DisplayName("Обновление статуса задачи: access denied")
    void testUpdateTaskStatusThrowsAccessDenied() {
        User user = new User()
                .setId(1L)
                .setRoles(Set.of(new Role().setName(RoleName.ROLE_USER)));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(CustomUserDetails.toUserDetails(user));

        task.setAssignee(new User().setId(2L));

        Long taskId = task.getId();
        String status = TaskStatus.PENDING.name();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        ErrorResponseException errorResponseException = assertThrows(ErrorResponseException.class,
                () -> taskService.updateTaskStatus(authentication, taskId, status));

        assertEquals(ErrorStatus.TASK_ACCESS_DENIED, errorResponseException.getErrorStatus());
    }

    @Test
    @DisplayName("Обновление статуса задачи: invalid variables")
    void testUpdateTaskStatusThrowsInvalidVariables() {
        User user = new User()
                .setId(1L)
                .setRoles(Set.of(new Role().setName(RoleName.ROLE_USER)));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(CustomUserDetails.toUserDetails(user));

        task.setAssignee(user);

        Long taskId = task.getId();
        String status = TaskStatus.PENDING.getName();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        ErrorResponseException errorResponseException = assertThrows(ErrorResponseException.class,
                () -> taskService.updateTaskStatus(authentication, taskId, status));

        assertEquals(ErrorStatus.TASK_INVALID_VARIABLE, errorResponseException.getErrorStatus());

    }
}
