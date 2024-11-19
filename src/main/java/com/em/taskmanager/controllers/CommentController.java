package com.em.taskmanager.controllers;

import com.em.taskmanager.dtos.CommentDto;
import com.em.taskmanager.exceptions.ErrorResponse;
import com.em.taskmanager.services.CommentService;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Управление комментариями", description = "Методы работы с комментариями")
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "Получить комментарии по идентификатору задачи",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema =
                            @Schema(implementation = CommentDto.class)))
                    ),
                    @ApiResponse(
                            description = "Провальный ответ", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping("/tasks/{taskId}/comments")
    public Page<CommentDto> getCommentsByTaskId(@PathVariable Long taskId,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return commentService.getCommentsByTaskId(taskId, page, pageSize);
    }


    @Operation(
            summary = "Получить комментарий по идентификатору",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CommentDto.class))
                    ),
                    @ApiResponse(
                            description = "Провальный ответ", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping("/comments/{id}")
    public CommentDto getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @Operation(
            summary = "Создать новый комментарий",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CommentDto.class))
                    ),
                    @ApiResponse(
                            description = "Провальный ответ", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping("/tasks/{taskId}/comments")
    public CommentDto addComment(@PathVariable Long taskId, @RequestBody @Validated CommentDto commentDto) {
        return commentService.addComment(taskId, commentDto);
    }

    @Operation(
            summary = "Удаление комментария по идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Комментарий успешно удален"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Комментарий не найден"
                    )
            }
    )
    @DeleteMapping("/comments/{id}")
    public void removeCommentById(@PathVariable Long id) {
        commentService.removeCommentById(id);
    }
}
