package com.arturk.taskmanagerms.entity;

import com.arturk.taskmanagerms.enums.TaskStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class TaskEntityBuilder {

    private Long id;
    private String title;
    private String description;
    private TaskStatusEnum status;
    private Integer priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TaskEntityBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public TaskEntityBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskEntityBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskEntityBuilder setStatus(TaskStatusEnum status) {
        this.status = status;
        return this;
    }

    public TaskEntityBuilder setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public TaskEntityBuilder setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public TaskEntityBuilder setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public TaskEntity build() {
        return new TaskEntity(
                this.id,
                this.title,
                this.description,
                this.status,
                this.priority,
                this.createdAt,
                this.updatedAt);
    }
}
