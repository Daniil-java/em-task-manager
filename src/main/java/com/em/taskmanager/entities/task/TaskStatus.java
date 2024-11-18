package com.em.taskmanager.entities.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    PENDING("в ожидании"),
    IN_PROGRESS("в процессе"),
    COMPLETED("завершено");

    private final String description;
}
