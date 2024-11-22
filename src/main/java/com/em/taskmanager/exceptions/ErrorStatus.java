package com.em.taskmanager.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    TASK_NOT_FOUND(HttpStatus.BAD_REQUEST, "Task Not Found!"),
    TASK_CREATION_ERROR(HttpStatus.BAD_REQUEST, "Task Creation Error! New task data cannot contain ID"),
    TASK_UPDATE_ERROR(HttpStatus.BAD_REQUEST, "Task Update Error! Task ID in the request body does not match the URL path variable!"),
    TASK_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Task Access Denied!"),
    TASK_ASSIGNEE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Task Access Denied! You do not have access to tasks that you are not the performer of!"),
    TASK_INVALID_VARIABLE(HttpStatus.BAD_REQUEST, "Task Invalid Variable!"),
    TASK_ASSIGNEE_NOT_FOUND(HttpStatus.FORBIDDEN, "A task must have an assignee"),


    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "Comment Not Found!"),
    COMMENT_CREATION_ERROR(HttpStatus.BAD_REQUEST, "Comment Creation Error! New comment data cannot contain ID"),
    COMMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Comment Access Denied!"),
    COMMENT_ASSIGNEE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Comment Access Denied! You do not have access to comments from tasks that you are not the performer of!"),


    USER_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "User not found!"),
    USER_USERNAME_EXISTS(HttpStatus.BAD_REQUEST, "Username already exists!" ),
    USER_EMAIL_EXISTS(HttpStatus.BAD_REQUEST, "Email already exists!"),
    ROLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "Role not found!"),
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "Invalid JWT token"),
    TOKEN_DATETIME_ERROR(HttpStatus.BAD_REQUEST, "Token datetime converting error!"),
    TOKEN_BUILD_ERROR(HttpStatus.BAD_REQUEST, "JWT Token build error!"),

    PARAM_INVALID_VALUE(HttpStatus.BAD_REQUEST, "Invalid param value!");

    private HttpStatus httpStatus;
    private String message;
}
