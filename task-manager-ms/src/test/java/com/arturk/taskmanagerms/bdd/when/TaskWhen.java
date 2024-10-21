package com.arturk.taskmanagerms.bdd.when;

import com.arturk.taskmanagerms.TaskManagerMsApplicationTests;
import com.arturk.taskmanagerms.bdd.FillObjectDataTable;
import com.arturk.taskmanagerms.bdd.IntegrationTestHelper;
import com.arturk.taskmanagerms.enums.TaskStatusEnum;
import com.arturk.taskmanagerms.model.TaskRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TaskWhen extends TaskManagerMsApplicationTests {

    @When("create task request was called")
    public void createTaskRequestCalled(DataTable dataTable) {
        when(getKafkaTemplate().send(any(), any(), any())).thenReturn(null);

        TaskRequest taskRequest = new TaskRequest();
        new FillObjectDataTable().fillObject(dataTable, taskRequest);

        ResponseEntity<Object> response = getRestTemplate().exchange(
                getUrl("/api/v4/task"),
                HttpMethod.POST,
                new HttpEntity<>(taskRequest, getDefaultHeaders()),
                Object.class);

        IntegrationTestHelper.getTestContext().setResponseObject(response,
                response.getBody() == null ? null : response.getBody().getClass());
    }

    @When("^delete task (\\S+) operation was called$")
    public void deleteTaskOperationCalled(String key) {
        ResponseEntity<Object> response = getRestTemplate().exchange(
                getUrl("/api/v4/task/" + IntegrationTestHelper.getTestContext().getTaskEntityCache().get(key)),
                HttpMethod.DELETE,
                new HttpEntity<>(null, getDefaultHeaders()),
                Object.class);

        IntegrationTestHelper.getTestContext().setResponseObject(response,
                response.getBody() == null ? null : response.getBody().getClass());
    }

    @When("^update task status operation for (?:task (\\S+)|taskId (\\d+)) and status (\\S+) was called$")
    public void updateTaskStatus(String key, Long taskId, TaskStatusEnum taskStatus) {
        when(getKafkaTemplate().send(any(), any(), any())).thenReturn(null);

        Long taskIdValue = key != null ? IntegrationTestHelper.getTestContext().getTaskEntityCache().get(key) : taskId;

        ResponseEntity<Object> response = getRestTemplate().exchange(
                getUrl("/api/v4/task/" + taskIdValue),
                HttpMethod.PUT,
                new HttpEntity<>(taskStatus, getDefaultHeaders()),
                Object.class);

        IntegrationTestHelper.getTestContext().setResponseObject(response,
                response.getBody() == null ? null : response.getBody().getClass());
    }

    @When("^update task status operation for task (\\S+) was called$")
    public void updateTask(String key, DataTable dataTable) {
        when(getKafkaTemplate().send(any(), any(), any())).thenReturn(null);

        TaskRequest taskRequest = new TaskRequest();
        new FillObjectDataTable().fillObject(dataTable, taskRequest);
        Long taskId = IntegrationTestHelper.getTestContext().getTaskEntityCache().get(key);

        ResponseEntity<Object> response = getRestTemplate().exchange(
                getUrl("/api/v4/task/" + taskId),
                HttpMethod.PATCH,
                new HttpEntity<>(taskRequest, getDefaultHeaders()),
                Object.class);

        IntegrationTestHelper.getTestContext().setResponseObject(response,
                response.getBody() == null ? null : response.getBody().getClass());
    }

    @When("get all tasks operation was called")
    public void getAllTasks() {
        ResponseEntity<Object> response = getRestTemplate().exchange(
                getUrl("/api/v4/task/all"),
                HttpMethod.GET,
                new HttpEntity<>(null, getDefaultHeaders()),
                Object.class);

        IntegrationTestHelper.getTestContext().setResponseObject(response,
                response.getBody() == null ? null : response.getBody().getClass());
    }

}
