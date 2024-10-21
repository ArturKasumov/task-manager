package com.arturk.taskmanagerms.entity.repository;

import com.arturk.taskmanagerms.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Query(value = "select count(*) from TaskEntity task " +
            "where task.title = :title and task.status not in ('COMPLETED', 'CANCELLED')")
    int checkDuplicates(@Param("title") String title);
}
