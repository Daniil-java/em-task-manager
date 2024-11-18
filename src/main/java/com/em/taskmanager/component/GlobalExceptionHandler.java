package com.em.taskmanager.component;

import com.em.taskmanager.exceptions.ErrorResponse;
import com.em.taskmanager.exceptions.ErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> catchAErrorResponsexception(ErrorResponseException e, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse()
                        .setStatus(e.getErrorStatus().getHttpStatus().value())
                        .setReasonPhrase(e.getErrorStatus().getHttpStatus().getReasonPhrase())
                        .setMessage(e.getErrorStatus().getMessage())
                        .setErrorCode(e.getErrorStatus())
                        .setAddress(ServletUriComponentsBuilder.fromCurrentRequest().toUriString())
                        .setCreated(Instant.now().getEpochSecond()),
                e.getErrorStatus().getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> catchAppException(Throwable e, WebRequest request) {
        log.error("Unknown error occurred", e);
        return new ResponseEntity<>(
                new ErrorResponse()
                        .setStatus(500)
                        .setMessage("Internal server error")
                        .setAddress(ServletUriComponentsBuilder.fromCurrentRequest().toUriString())
                        .setCreated(Instant.now().getEpochSecond()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
