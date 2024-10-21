package com.arturk.taskmanagerms.model;

import com.arturk.taskmanagerms.enums.TaskStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDto {

    private Long id;

    private String title;

    private String description;

    private TaskStatusEnum status;

    private Integer priority;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
