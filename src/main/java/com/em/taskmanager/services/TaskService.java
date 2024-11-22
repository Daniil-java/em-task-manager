package com.em.taskmanager.services;

import com.em.taskmanager.dtos.CustomUserDetails;
import com.em.taskmanager.dtos.TaskDto;
import com.em.taskmanager.dtos.TaskFilterDto;
import com.em.taskmanager.dtos.mappers.TaskMapper;
import com.em.taskmanager.entities.RoleName;
import com.em.taskmanager.entities.User;
import com.em.taskmanager.entities.task.Task;
import com.em.taskmanager.entities.task.TaskStatus;
import com.em.taskmanager.exceptions.ErrorResponseException;
import com.em.taskmanager.exceptions.ErrorStatus;
import com.em.taskmanager.repositories.TaskRepository;
import com.em.taskmanager.repositories.specifications.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskDto getTaskDtoById(Long taskId) {
        return taskMapper.toDto(getTaskById(taskId));
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.TASK_NOT_FOUND));
    }

    public TaskDto addTask(Authentication authentication, TaskDto taskDto) {
        if (taskDto.getId() != null) {
            throw new ErrorResponseException(ErrorStatus.TASK_CREATION_ERROR);
        }

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        User author = userService.getUserById(user.getId());

        return taskMapper.toDto(
                taskRepository.save(taskMapper.toEntity(taskDto).setAuthor(author))
        );
    }

    public TaskDto editTask(Long id, TaskDto taskDto) {
        if (!taskDto.getId().equals(id)) {
            throw new ErrorResponseException(ErrorStatus.TASK_UPDATE_ERROR);
        }

        checkTaskExistsById(id);

        return taskMapper.toDto(
                taskRepository.save(taskMapper.toEntity(taskDto)));
    }

    public void removeTaskById(Long taskId) {
        checkTaskExistsById(taskId);

        taskRepository.deleteById(taskId);
    }

    public void checkTaskExistsById(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ErrorResponseException(ErrorStatus.TASK_NOT_FOUND);
        }
    }

    public Page<TaskDto> getTasks(Authentication authentication, TaskFilterDto taskFilterDto) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        if (!user.hasRole(RoleName.ROLE_ADMIN)
                && taskFilterDto.getAssigneeId() != user.getId()) {
            throw new ErrorResponseException(ErrorStatus.TASK_ASSIGNEE_ACCESS_DENIED);
        }

        Specification<Task> spec = TaskSpecification.createSpecification(taskFilterDto);

        return taskRepository.findAll(
                spec, PageRequest.of(taskFilterDto.getPage(), taskFilterDto.getPageSize())
                ).map(taskMapper::toDto);
    }

    @Transactional
    public TaskDto updateTaskStatus(Authentication authentication, Long id, String status) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Task task = getTaskById(id);

        if (!user.hasRole(RoleName.ROLE_ADMIN)) {
            if (task.getAssignee() == null) {
                throw new ErrorResponseException(ErrorStatus.TASK_ASSIGNEE_ACCESS_DENIED);
            }

            if (!task.getAssignee().getId().equals(user.getId())) {
                throw new ErrorResponseException(ErrorStatus.TASK_ACCESS_DENIED);
            }
        }

        try {
            task.setStatus(TaskStatus.valueOf(status));
            return taskMapper.toDto(taskRepository.save(task));
        } catch (IllegalArgumentException e) {
            throw new ErrorResponseException(ErrorStatus.TASK_INVALID_VARIABLE);
        }
    }
}
