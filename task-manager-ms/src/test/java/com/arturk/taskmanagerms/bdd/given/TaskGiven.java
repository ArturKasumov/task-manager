package com.arturk.taskmanagerms.bdd.given;

import com.arturk.taskmanagerms.TaskManagerMsApplicationTests;
import com.arturk.taskmanagerms.bdd.FillObjectDataTable;
import com.arturk.taskmanagerms.bdd.IntegrationTestHelper;
import com.arturk.taskmanagerms.entity.TaskEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@CucumberContextConfiguration
public class TaskGiven extends TaskManagerMsApplicationTests {

    @Given("^task(?: (\\S+)|) exists$")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void taskExists(String key, DataTable dataTable) {
        TaskEntity taskEntity = new TaskEntity();
        new FillObjectDataTable().fillObject(dataTable, taskEntity);
        taskEntity = getTaskRepository().save(taskEntity);
        if (key != null) {
            IntegrationTestHelper.getTestContext().getTaskEntityCache().put(key, taskEntity.getId());
        }
    }

    @Given("tasks do not exist")
    public void deleteAllTasks() {
        getTaskRepository().deleteAll();
    }
}
