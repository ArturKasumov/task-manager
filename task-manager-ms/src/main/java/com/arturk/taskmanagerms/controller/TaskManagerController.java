package com.arturk.taskmanagerms.controller;

import com.arturk.taskmanagerms.convertor.TaskConvertor;
import com.arturk.taskmanagerms.entity.TaskEntity;
import com.arturk.taskmanagerms.enums.TaskStatusEnum;
import com.arturk.taskmanagerms.exception.RestException;
import com.arturk.taskmanagerms.model.TaskDto;
import com.arturk.taskmanagerms.model.TaskRequest;
import com.arturk.taskmanagerms.service.TaskManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v4/task")
@Tag(name = "TaskManager", description = "The TaskManager API")
public class TaskManagerController {

    private final TaskManagerService taskManagerService;
    private final TaskConvertor taskConvertor;

    @Operation(summary = "Creates a new task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task is created",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(description = "Task id", implementation = Long.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RestException.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RestException.class))
                    })
    })
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for creation a new task",
                    required = true,
                    content = @Content)
            @RequestBody TaskRequest request) {
        TaskEntity taskEntity = taskManagerService.createTask(request);
        return ResponseEntity.ok(taskEntity.getId());
    }

    @Operation(summary = "Deletes the task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task is deleted"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RestException.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RestException.class))
                    })
    })
    @RequestMapping(value = "/{taskId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTask(@Parameter(name = "taskId",
            description = "Id of task to be deleted",
            required = true, in = ParameterIn.PATH)
                                           @PathVariable Long taskId) {
        taskManagerService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Updates status of the task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task`s status is updates"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RestException.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RestException.class))
                    })
    })
    @RequestMapping(value = "/{taskId}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTaskStatus(@Parameter(name = "taskId",
            description = "Id of task to be updated",
            required = true, in = ParameterIn.PATH)
                                                 @PathVariable Long taskId,
                                                 @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                         description = "New status for task",
                                                         required = true,
                                                         content = @Content)
                                                 @RequestBody TaskStatusEnum newTaskStatus) {
        taskManagerService.updateTaskStatus(taskId, newTaskStatus);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Updates a task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task is updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RestException.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RestException.class))
                    })
    })

    @RequestMapping(value = "/{taskId}", method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTask(@Parameter(name = "taskId",
            description = "Id of task to be updated",
            required = true, in = ParameterIn.PATH)
                                           @PathVariable Long taskId,
                                           @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                   description = "Data for updating a task",
                                                   required = true,
                                                   content = @Content)
                                           @RequestBody TaskRequest request) {
        taskManagerService.updateTask(taskId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Returns all tasks")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tasks are returned",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(description = "Task", implementation = TaskDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RestException.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RestException.class))
                    })
    })
    @RequestMapping(value = "/all", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskEntity> taskEntities = taskManagerService.getAllTasks();
        List<TaskDto> result = new ArrayList<>();
        taskEntities.forEach(entity -> result.add(taskConvertor.toDTO(entity)));
        return ResponseEntity.ok(result);
    }
}
