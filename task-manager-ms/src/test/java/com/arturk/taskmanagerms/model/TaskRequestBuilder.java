package com.arturk.taskmanagerms.model;

import com.arturk.taskmanagerms.enums.TaskStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TaskRequestBuilder {

    private String title;
    private String description;
    private TaskStatusEnum status;
    private Integer priority;

    public TaskRequestBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskRequestBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskRequestBuilder setStatus(TaskStatusEnum status) {
        this.status = status;
        return this;
    }

    public TaskRequestBuilder setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public TaskRequest build() {
        return new TaskRequest(
                this.title,
                this.description,
                this.status,
                this.priority);
    }
}
