package com.arturk.taskmanagerms.convertor;


import com.arturk.taskmanagerms.TaskManagerMsApplicationTests;
import com.arturk.taskmanagerms.entity.TaskEntity;
import com.arturk.taskmanagerms.enums.TaskStatusEnum;
import com.arturk.taskmanagerms.model.TaskDto;
import com.arturk.taskmanagerms.model.TaskRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskConvertorTest extends TaskManagerMsApplicationTests {

    @Autowired
    private TaskConvertor taskConvertor;

    @Test
    public void toTaskEntityTest() {
        TaskRequest request = new TaskRequest();
        request.setTitle("title");
        request.setDescription("description");
        request.setPriority(5);
        request.setStatus(TaskStatusEnum.NEW);

        TaskEntity taskEntity = new TaskEntity();
        taskConvertor.fromDTO(request, taskEntity);

        assertEquals("title", taskEntity.getTitle());
        assertEquals("description", taskEntity.getDescription());
        assertEquals(5, taskEntity.getPriority());
        assertEquals(TaskStatusEnum.NEW, taskEntity.getStatus());
    }

    @Test
    public void toTaskEntityWhenStatusNotSetTest() {
        TaskRequest request = new TaskRequest();
        request.setTitle("title");
        request.setDescription("description");
        request.setPriority(5);

        TaskEntity taskEntity = new TaskEntity();
        taskConvertor.fromDTO(request, taskEntity);

        assertEquals("title", taskEntity.getTitle());
        assertEquals("description", taskEntity.getDescription());
        assertEquals(5, taskEntity.getPriority());
        assertEquals(TaskStatusEnum.NEW, taskEntity.getStatus());
    }

    @Test
    public void toTaskDtoTest() {
        LocalDateTime now = LocalDateTime.now();

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setTitle("title");
        taskEntity.setDescription("description");
        taskEntity.setPriority(5);
        taskEntity.setStatus(TaskStatusEnum.NEW);
        taskEntity.setCreatedAt(now);

        TaskDto dto = taskConvertor.toDTO(taskEntity);

        assertEquals(1L, dto.getId());
        assertEquals("title", dto.getTitle());
        assertEquals("description", dto.getDescription());
        assertEquals(5, dto.getPriority());
        assertEquals(TaskStatusEnum.NEW, dto.getStatus());
        assertEquals(now, dto.getCreatedAt());
    }
}
