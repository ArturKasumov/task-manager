package com.arturk.taskmanagerms.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

public class DataSourceRouting extends AbstractRoutingDataSource {

    private final DataSource postgresDataSource;
    private final DataSource h2DataSource;

    public DataSourceRouting(DataSource postgresDataSource, DataSource h2DataSource) {
        this.postgresDataSource = postgresDataSource;
        this.h2DataSource = h2DataSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DatabaseContextHolder.INSTANCE.getDatabase();
    }

    @Override
    public DataSource determineTargetDataSource() {
        return DatabaseContextHolder.INSTANCE.getDatabase().equals(DatabaseEnum.POSTGRES) ? postgresDataSource : h2DataSource;
    }
}