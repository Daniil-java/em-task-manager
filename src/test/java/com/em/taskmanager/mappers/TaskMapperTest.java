package com.em.taskmanager.mappers;

import com.em.taskmanager.dtos.TaskDto;
import com.em.taskmanager.dtos.mappers.TaskMapper;
import com.em.taskmanager.entities.task.Task;
import com.em.taskmanager.entities.task.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TaskMapperTest {
    @Autowired
    private TaskMapper taskMapper;

    @Test
    void testToDto() {
        Task task = new Task()
                .setId(1L)
                .setTitle("Test Task")
                .setStatus(TaskStatus.IN_PROGRESS);

        TaskDto taskDto = taskMapper.toDto(task);

        assertNotNull(taskDto);
        assertEquals(task.getId(), taskDto.getId());
        assertEquals(task.getTitle(), taskDto.getTitle());
    }

    @Test
    void testToEntity() {
        TaskDto taskDto = new TaskDto()
                .setId(1L)
                .setTitle("Test Task")
                .setStatus(TaskStatus.IN_PROGRESS);

        Task task = taskMapper.toEntity(taskDto);

        assertNotNull(task);
        assertEquals(taskDto.getId(), task.getId());
        assertEquals(taskDto.getTitle(), task.getTitle());
    }

    @Test
    void testToDtoList() {
        List<Task> taskList = Collections.singletonList(new Task());
        List<TaskDto> taskDtoList = taskMapper.toDtoList(taskList);

        assertNotNull(taskDtoList);
        assertEquals(taskList.size(), taskDtoList.size());
    }

    @Test
    void testToEntityList() {
        List<TaskDto> taskDtoList = Collections.singletonList(new TaskDto());
        List<Task> taskList = taskMapper.toEntityList(taskDtoList);

        assertNotNull(taskList);
        assertEquals(taskDtoList.size(), taskList.size());
    }
}
