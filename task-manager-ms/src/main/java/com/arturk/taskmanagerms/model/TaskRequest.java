package com.arturk.taskmanagerms.model;

import com.arturk.taskmanagerms.enums.TaskStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    @NotEmpty(message = "Title can not be empty")
    private String title;

    private String description;

    @Schema(description = "status",
            type = "string",
            allowableValues = {"NEW, IN_PROGRESS, COMPLETED, CANCELLED"},
            example = "NEW")
    private TaskStatusEnum status = TaskStatusEnum.NEW;

    @NotNull(message = "Priority can not be empty")
    @Min(value = 1, message = "Priority must be at least 1")
    @Max(value = 10, message = "Priority must be at most 10")
    private Integer priority;
}
