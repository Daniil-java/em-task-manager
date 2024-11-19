package com.em.taskmanager.services;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.em.taskmanager.dtos.TaskDto;
import com.em.taskmanager.dtos.TaskFilterDto;
import com.em.taskmanager.dtos.mappers.TaskMapper;
import com.em.taskmanager.entities.task.Task;
import com.em.taskmanager.exceptions.ErrorResponseException;
import com.em.taskmanager.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

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
    @DisplayName("Проверка на отсутствие ошибок, при корректных данных")
    void testGetTaskDtoById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        assertDoesNotThrow(() -> {
            taskService.getTaskDtoById(1L);
        });
    }

    @Test
    @DisplayName("Проверка на корректную обработку поступаемых данных, при создании новой задачи")
    void testAddTask() {
        taskDto.setId(null);
        when(taskMapper.toEntity(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.addTask(taskDto);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Проверка на корректную обработку поступаемых данных, при обновлении существующей задачи")
    void testEditTask() {
        taskDto.setId(1L);
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskMapper.toEntity(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.editTask(1L, taskDto);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Проверка на отсутствие непредусмотренных ошибок")
    void testRemoveTaskById() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> taskService.removeTaskById(1L));
    }

    @Test
    @DisplayName("Проверка на выпадение ошибки")
    void testTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ErrorResponseException.class, () -> taskService.getTaskDtoById(1L));
    }

    @Test
    @DisplayName("Проверка на корректный возврат Page")
    void testGetTasks() {
        PageRequest pageRequest = PageRequest.of(taskFilterDto.getPage(), taskFilterDto.getPageSize());
        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));

        when(taskRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(taskPage);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        Page<TaskDto> result = taskService.getTasks(taskFilterDto);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}
