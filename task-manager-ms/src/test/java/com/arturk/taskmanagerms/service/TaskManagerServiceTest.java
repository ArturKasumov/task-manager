package com.arturk.taskmanagerms.service;

import com.arturk.taskmanagerms.convertor.TaskConvertor;
import com.arturk.taskmanagerms.entity.TaskEntity;
import com.arturk.taskmanagerms.entity.TaskEntityBuilder;
import com.arturk.taskmanagerms.entity.repository.TaskRepository;
import com.arturk.taskmanagerms.enums.TaskStatusEnum;
import com.arturk.taskmanagerms.exception.TaskNotFoundException;
import com.arturk.taskmanagerms.model.TaskRequest;
import com.arturk.taskmanagerms.model.TaskRequestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskManagerServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskConvertor taskConvertor;

    @Mock
    private KafkaTemplate<?,?> kafkaTemplate;

    @InjectMocks
    private TaskManagerService taskManagerService;

    @Test
    public void createTaskTest() {
        TaskRequest request = new TaskRequestBuilder()
                .setTitle("title")
                .setDescription("description")
                .setPriority(1)
                .setStatus(TaskStatusEnum.NEW)
                .build();

        TaskEntity entity = new TaskEntityBuilder()
                .setId(1L)
                .setTitle("title")
                .setDescription("description")
                .setPriority(1)
                .setStatus(TaskStatusEnum.NEW)
                .build();

        when(kafkaTemplate.send(any(), any(), any())).thenReturn(null);
        when(taskRepository.save(any())).thenReturn(entity);

        taskManagerService.createTask(request);

        verify(taskConvertor, Mockito.times(1)).fromDTO(any(), any());
        verify(taskRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void updateTaskStatusTest() {
        TaskEntity entity = new TaskEntityBuilder()
                .setId(1L)
                .setTitle("title")
                .setDescription("description")
                .setPriority(1)
                .setStatus(TaskStatusEnum.NEW)
                .build();

        when(kafkaTemplate.send(any(), any(), any())).thenReturn(null);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(taskRepository.save(entity)).thenReturn(entity);

        taskManagerService.updateTaskStatus(1L, TaskStatusEnum.IN_PROGRESS);

        verify(taskRepository, Mockito.times(1)).findById(1L);
        verify(taskRepository, Mockito.times(1)).save(entity);
    }

    @Test
    public void updateTaskStatusWhenTaskNotExistsTest() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskManagerService.updateTaskStatus(1L, TaskStatusEnum.IN_PROGRESS));

        verify(taskRepository, Mockito.times(1)).findById(1L);
        verify(taskRepository, Mockito.times(0)).save(any());
    }

    @Test
    public void updateTaskTest() {
        TaskRequest request = new TaskRequestBuilder()
                .setDescription("newDescription")
                .build();

        TaskEntity entity = new TaskEntityBuilder()
                .setId(1L)
                .setTitle("title")
                .setDescription("description")
                .setPriority(1)
                .setStatus(TaskStatusEnum.NEW)
                .build();

        when(kafkaTemplate.send(any(), any(), any())).thenReturn(null);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(taskRepository.save(entity)).thenReturn(entity);

        taskManagerService.updateTask(1L, request);

        verify(taskConvertor, Mockito.times(1)).fromDTO(request, entity);
        verify(taskRepository, Mockito.times(1)).save(entity);
    }
}
