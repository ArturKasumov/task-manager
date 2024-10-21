package com.arturk.taskmanagerms.service;

import com.arturk.taskmanagerms.convertor.TaskConvertor;
import com.arturk.taskmanagerms.entity.TaskEntity;
import com.arturk.taskmanagerms.entity.repository.TaskRepository;
import com.arturk.taskmanagerms.enums.TaskStatusEnum;
import com.arturk.taskmanagerms.exception.TaskAlreadyExistsException;
import com.arturk.taskmanagerms.exception.TaskNotFoundException;
import com.arturk.taskmanagerms.model.TaskRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class TaskManagerService {

    private final TaskRepository taskRepository;
    private final TaskConvertor taskConvertor;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String kafkaTopicName;

    public TaskEntity createTask(@Valid TaskRequest request) {
        log.debug("Creating task started");

        int duplicates = taskRepository.checkDuplicates(request.getTitle());
        if (duplicates > 0) {
            throw new TaskAlreadyExistsException("Task with such title already exists");
        }
        TaskEntity taskEntity = new TaskEntity();
        taskConvertor.fromDTO(request, taskEntity);
        taskEntity = taskRepository.save(taskEntity);

        kafkaTemplate.send(kafkaTopicName, String.valueOf(taskEntity.getId()), taskEntity);
        log.debug("Creating task finished successfully");
        return taskEntity;
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    public void updateTaskStatus(Long taskId, TaskStatusEnum newTaskStatus) {
        log.debug("Updating task status started");

        TaskEntity taskEntity = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
        taskEntity.setStatus(newTaskStatus);
        taskEntity = taskRepository.save(taskEntity);

        kafkaTemplate.send(kafkaTopicName, String.valueOf(taskEntity.getId()), taskEntity);
        log.debug("Updating task status finished successfully");
    }

    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    public void updateTask(Long taskId, @Valid TaskRequest request) {
        log.debug("Updating task started");

        TaskEntity taskEntity = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
        taskConvertor.fromDTO(request, taskEntity);
        taskEntity = taskRepository.save(taskEntity);

        kafkaTemplate.send(kafkaTopicName, String.valueOf(taskEntity.getId()), taskEntity);
        log.debug("Updating task finished successfully");
    }
}
