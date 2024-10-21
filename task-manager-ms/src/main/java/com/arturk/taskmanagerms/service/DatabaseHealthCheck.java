package com.arturk.taskmanagerms.service;

import com.arturk.taskmanagerms.config.DataSourceRouting;
import com.arturk.taskmanagerms.config.DatabaseContextHolder;
import com.arturk.taskmanagerms.config.DatabaseEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
@Slf4j
public class DatabaseHealthCheck {

    @Autowired
    private DataSourceRouting routingDataSource;

    @Scheduled(fixedDelay = 3_000)
    public void checkPrimaryPostgres() {
        log.info("Checking current datasource: {}", DatabaseContextHolder.INSTANCE.getDatabase());

        try (Connection connection = routingDataSource.determineTargetDataSource().getConnection()) {
            connection.createStatement().executeQuery("SELECT 1");
        } catch (Exception e) {
            log.info("Connection to {} failed. Switching to POSTGRES", DatabaseContextHolder.INSTANCE.getDatabase());
            DatabaseContextHolder.INSTANCE.setDatabaseEnum(DatabaseEnum.POSTGRES);
            log.info("Switched to POSTGRES database");
        }
    }
}
