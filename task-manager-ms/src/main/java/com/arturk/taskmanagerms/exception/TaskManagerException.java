package com.arturk.taskmanagerms.exception;

import lombok.Getter;

@Getter
public abstract class TaskManagerException extends RuntimeException {
    private final String code;
    private final String message;
    private final String details;

    public TaskManagerException(String code, String message) {
        this(code, message, null);
    }

    public TaskManagerException(String code, String message, String details) {
        super(message);
        this.code = code;
        this.message = message;
        this.details = details;
    }
}
