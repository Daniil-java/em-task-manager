package com.em.taskmanager.entities.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskPriority {
    MUST,
    SHOULD,
    COULD,
    WOULD;
}