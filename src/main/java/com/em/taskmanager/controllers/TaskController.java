package com.em.taskmanager.controllers;

import com.em.taskmanager.dtos.TaskDto;
import com.em.taskmanager.dtos.TaskFilterDto;
import com.em.taskmanager.exceptions.ErrorResponse;
import com.em.taskmanager.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Управление задачами", description = "Методы работы с задачами")
public class TaskController {

    private final TaskService taskService;

    @Operation(
            summary = "Получение задач по заданному фильтру",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema =
                            @Schema(implementation = TaskDto.class)))
                    ),
                    @ApiResponse(
                            description = "Провальный ответ", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping
    public Page<TaskDto> getTasks(@ModelAttribute TaskFilterDto taskFilterDto) {
        return taskService.getTasks(taskFilterDto);
    }

    @Operation(
            summary = "Получение задачи по её идентификатору",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TaskDto.class))
                    ),
                    @ApiResponse(
                            description = "Провальный ответ", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskDtoById(id);
    }

    @Operation(
            summary = "Добавление новой задачи",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TaskDto.class))
                    ),
                    @ApiResponse(
                            description = "Провальный ответ", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping
    public TaskDto addTask(@RequestBody @Validated TaskDto taskDto) {
        return taskService.addTask(taskDto);
    }

    @Operation(
            summary = "Обновление задачи",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TaskDto.class))
                    ),
                    @ApiResponse(
                            description = "Провальный ответ", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody @Validated TaskDto taskDto) {
        return taskService.editTask(id, taskDto);
    }

    @Operation(
            summary = "Удаление задачи по идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Задача успешно удалена"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Задача не найдена"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public void removeTaskById(@PathVariable Long id) {
        taskService.removeTaskById(id);
    }


}
