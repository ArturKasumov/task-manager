package com.arturk.taskmanagerms.bdd;

import com.arturk.taskmanagerms.entity.TaskEntity;
import com.arturk.taskmanagerms.enums.TaskStatusEnum;
import com.arturk.taskmanagerms.exception.RestException;
import com.arturk.taskmanagerms.model.TaskDto;
import io.cucumber.java.DataTableType;

import java.util.Map;

public class DataTableTypes {

    @DataTableType
    public RestException mapRestException(Map<String, String> entry) {
        return new RestException(entry.get("code"), entry.get("message"), entry.get("details"));
    }

    @DataTableType
    public TaskDto mapTaskEntity(Map<String, String> entry) {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle(entry.get("title"));
        taskDto.setDescription(entry.get("description"));
        taskDto.setPriority(Integer.valueOf(entry.get("priority")));
        if (entry.get("status") != null) {
            taskDto.setStatus(TaskStatusEnum.valueOf(entry.get("status")));
        }
        return taskDto;
    }
}
