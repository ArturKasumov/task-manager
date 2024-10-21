package com.arturk.taskmanagerms.service;

import com.arturk.taskmanagerms.exception.BusinessException;
import com.arturk.taskmanagerms.exception.RestException;
import com.arturk.taskmanagerms.exception.TaskManagerException;
import com.arturk.taskmanagerms.exception.TechnicalException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerService {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<RestException>> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("ConstraintViolationException", ex);
        List<RestException> result = new ArrayList<>();
        ex.getConstraintViolations().forEach(item -> {
            log.error("Validation error: {}", item.getMessage());
            result.add(new RestException(null, item.getMessage(), null));
        });
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<RestException> handleCustomerNotFound(BusinessException exception) {
        log.error("Internal TaskManagerException occurred", exception);
        RestException restException = new RestException(exception.getCode(), exception.getMessage(), exception.getDetails());
        return new ResponseEntity<>(restException, HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<RestException> handleCustomerNotFound(TechnicalException exception) {
        log.error("Internal TaskManagerException occurred", exception);
        RestException restException = new RestException(exception.getCode(), exception.getMessage(), exception.getDetails());
        return new ResponseEntity<>(restException, HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestException> handleCustomerNotFound(Exception exception) {
        log.error("Internal error occurred", exception);
        RestException restException = new RestException(null, exception.getMessage(), null);
        return new ResponseEntity<>(restException, HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
