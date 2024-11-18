package com.em.taskmanager.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    TASK_ERROR(HttpStatus.BAD_REQUEST, "Task Error!"),
    TASK_NOT_FOUND(HttpStatus.BAD_REQUEST, "Task Not Found!"),
    TASK_CREATION_ERROR(HttpStatus.BAD_REQUEST, "Task Creation Error!"),
    TASK_UPDATE_ERROR(HttpStatus.BAD_REQUEST, "Task Update Error!"),

    USER_CREATION_ERROR(HttpStatus.BAD_REQUEST, "User Creation Error!"),
    USER_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "User not found!");

    private HttpStatus httpStatus;
    private String message;
}
