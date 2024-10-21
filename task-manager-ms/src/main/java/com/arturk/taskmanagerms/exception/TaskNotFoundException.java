package com.arturk.taskmanagerms.exception;

public class TaskNotFoundException extends BusinessException {
    private final static String code = "TE-001";

    public TaskNotFoundException() {
        this(null);
    }

    public TaskNotFoundException(String details) {
        super(code, "Task is not found", details);
    }
}
