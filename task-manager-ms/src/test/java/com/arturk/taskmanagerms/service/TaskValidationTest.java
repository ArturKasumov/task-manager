package com.arturk.taskmanagerms.service;

import com.arturk.taskmanagerms.TaskManagerMsApplicationTests;
import com.arturk.taskmanagerms.enums.TaskStatusEnum;
import com.arturk.taskmanagerms.model.TaskRequest;
import com.arturk.taskmanagerms.model.TaskRequestBuilder;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskValidationTest extends TaskManagerMsApplicationTests {

    @Autowired
    private LocalValidatorFactoryBean factory;

    @Test
    public void validateTaskRequestWhenTitleNullTest() {
        TaskRequest request = new TaskRequestBuilder()
                .setTitle(null)
                .setDescription("desc")
                .setPriority(1)
                .setStatus(TaskStatusEnum.NEW).build();

        Set<ConstraintViolation<TaskRequest>> violations = factory.getValidator().validate(request);

        Assertions.assertFalse(violations.isEmpty());

        Set<String> violationMessages = new HashSet<>();

        for (ConstraintViolation<TaskRequest> violation : violations) {
            violationMessages.add(violation.getMessage());
        }

        assertTrue(violationMessages.contains("Title can not be empty"));
    }

    @Test
    public void validateTaskRequestWhenPriorityGreater10Test() {
        TaskRequest request = new TaskRequestBuilder()
                .setTitle("title")
                .setDescription("desc")
                .setPriority(11)
                .setStatus(TaskStatusEnum.NEW).build();

        Set<ConstraintViolation<TaskRequest>> violations = factory.getValidator().validate(request);

        Assertions.assertFalse(violations.isEmpty());

        Set<String> violationMessages = new HashSet<>();

        for (ConstraintViolation<TaskRequest> violation : violations) {
            violationMessages.add(violation.getMessage());
        }

        assertTrue(violationMessages.contains("Priority must be at most 10"));
    }

    @Test
    public void validateTaskRequestWhenPriorityAndTitleNullTest() {
        TaskRequest request = new TaskRequestBuilder()
                .setTitle(null)
                .setDescription("desc")
                .setPriority(null)
                .setStatus(TaskStatusEnum.NEW).build();

        Set<ConstraintViolation<TaskRequest>> violations = factory.getValidator().validate(request);

        Assertions.assertFalse(violations.isEmpty());

        Set<String> violationMessages = new HashSet<>();

        for (ConstraintViolation<TaskRequest> violation : violations) {
            violationMessages.add(violation.getMessage());
        }

        assertTrue(violationMessages.contains("Title can not be empty"));
        assertTrue(violationMessages.contains("Priority can not be empty"));
    }
}
