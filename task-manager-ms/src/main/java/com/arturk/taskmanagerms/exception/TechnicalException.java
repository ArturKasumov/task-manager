package com.arturk.taskmanagerms.exception;

public class TechnicalException extends TaskManagerException {
    public TechnicalException(String code, String message) {
        super(code, message);
    }

    public TechnicalException(String code, String message, String details) {
        super(code, message, details);
    }
}
