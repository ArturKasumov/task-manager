package com.arturk.taskmanagerms.bdd.then;

import com.arturk.taskmanagerms.TaskManagerMsApplicationTests;
import com.arturk.taskmanagerms.bdd.FillObjectDataTable;
import com.arturk.taskmanagerms.bdd.IntegrationTestHelper;
import com.arturk.taskmanagerms.entity.TaskEntity;
import com.arturk.taskmanagerms.exception.RestException;
import com.arturk.taskmanagerms.model.TaskDto;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TaskThen extends TaskManagerMsApplicationTests {

    @Then("rest operation should be executed successfully")
    public void parmaRestOperationShouldBeExecutedSuccessfully() {
        ResponseEntity<?> response = (ResponseEntity<?>) IntegrationTestHelper.getTestContext().getResponseObject();
        assertEquals(200, response.getStatusCode().value());
    }

    @Then("parma rest operation should be executed with {int} status")
    public void parmaRestOperationShouldBeExecutedWithStatus(int statusCode) {
        ResponseEntity<?> response = (ResponseEntity<?>) IntegrationTestHelper.getTestContext().getResponseObject();
        assertEquals(statusCode, response.getStatusCode().value());
    }

    @Then("task should be created with next fields")
    public void taskShouldBeCreated(DataTable dataTable) {
        TaskEntity compareTo = new TaskEntity();
        new FillObjectDataTable().fillObject(dataTable, compareTo);

        ResponseEntity<?> response = (ResponseEntity<?>) IntegrationTestHelper.getTestContext().getResponseObject();

        assertNotNull(response.getBody());
        assertInstanceOf(Integer.class, response.getBody(), "Incorrect body response type");

        Integer taskId = (Integer) response.getBody();
        TaskEntity taskEntity = getTaskRepository().findById(Long.valueOf(taskId))
                .orElseThrow(() -> new RuntimeException("Task with id: " + taskId + " not found"));

        assertEquals(compareTo.getTitle(), taskEntity.getTitle());
        assertEquals(compareTo.getDescription(), taskEntity.getDescription());
        assertEquals(compareTo.getStatus(), taskEntity.getStatus());
        assertEquals(compareTo.getPriority(), taskEntity.getPriority());
        assertTrue(taskEntity.getCreatedAt().isBefore(LocalDateTime.now()));
    }

    @Then("^task (\\S+) should not exist$")
    public void taskShouldNotExist(String key) {
        Long taskId = IntegrationTestHelper.getTestContext().getTaskEntityCache().get(key);

        Optional<TaskEntity> taskEntityOptional = getTaskRepository().findById(taskId);

        assertTrue(taskEntityOptional.isEmpty());
    }

    @Then("response contains the following error\\(s)")
    public void responseErrorsContentCheck(List<RestException> errors) {
        ResponseEntity<?> response = (ResponseEntity<?>) IntegrationTestHelper.getTestContext().getResponseObject();
        assertNotNull(response.getBody());

        if (response.getBody() instanceof List) {
            List<LinkedHashMap<?, ?>> responseErrors = (List) response.getBody();
            checkErrorsAreEqual(responseErrors, errors);
        } else if (response.getBody() instanceof Map) {
            LinkedHashMap<?, ?> responseError = (LinkedHashMap<?, ?>) response.getBody();
            checkErrorsAreEqual(List.of(responseError), errors);
        } else {
            throw new RuntimeException("Unsupported case for now");
        }
    }

    private void checkErrorsAreEqual(List<LinkedHashMap<?, ?>> responseErrors, List<RestException> compareWith) {
        for (RestException error : compareWith) {
            List<LinkedHashMap<?, ?>> collect = responseErrors.stream()
                    .filter(item -> Objects.equals(((Map<?, ?>) item).get("code"), error.getCode())
                            && Objects.equals(((Map<?, ?>) item).get("message"), error.getMessage())
                            && Objects.equals(((Map<?, ?>) item).get("details"), error.getDetails()))
                    .toList();
            assertEquals(1, collect.size(),
                    "Error not found. Code: " + error.getCode() + ", message: " + error.getMessage()
                            + ", details: " + error.getDetails() + ".\nOriginal list of errors is " + responseErrors);
        }
    }

    @Then("^task (\\S+) should have next fields$")
    public void taskShouldHaveNextField(String key, DataTable dataTable) {
        TaskEntity compareTo = new TaskEntity();
        new FillObjectDataTable().fillObject(dataTable, compareTo);

        Long taskId = IntegrationTestHelper.getTestContext().getTaskEntityCache().get(key);
        TaskEntity taskEntity = getTaskRepository().findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task with id: " + taskId + " not found"));

        assertEquals(compareTo.getTitle(), taskEntity.getTitle());
        assertEquals(compareTo.getDescription(), taskEntity.getDescription());
        assertEquals(compareTo.getStatus(), taskEntity.getStatus());
        assertEquals(compareTo.getPriority(), taskEntity.getPriority());
    }

    @Then("tasks should be returned")
    public void taskShouldBeReturned(List<TaskDto> dtos) {
        List<TaskEntity> allTasks = getTaskRepository().findAll();

        assertEquals(dtos.size(), allTasks.size());

        for (TaskDto dto : dtos) {
            assertTrue(
                    allTasks.stream().anyMatch(task -> Objects.equals(dto.getTitle(), task.getTitle())
                            && Objects.equals(dto.getDescription(), task.getDescription())
                            && Objects.equals(dto.getPriority(), task.getPriority())
                            && Objects.equals(dto.getStatus(), task.getStatus()))
            );
        }

    }
}
