package com.arturk.taskmanagerms.exception;

public class TaskAlreadyExistsException extends BusinessException {

    private final static String code = "TE-002";

    public TaskAlreadyExistsException() {
        this(null);
    }

    public TaskAlreadyExistsException(String details) {
        super(code, "Task already exists", details);
    }

}
