package com.arturk.taskmanagerms.exception;

public class BusinessException extends TaskManagerException {

    public BusinessException(String code, String message) {
        super(code, message);
    }

    public BusinessException(String code, String message, String details) {
        super(code, message, details);
    }
}
