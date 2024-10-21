package com.arturk.taskmanagerms.config;

import lombok.Getter;

@Getter
public enum DatabaseContextHolder {

    INSTANCE;

    private DatabaseEnum database = DatabaseEnum.H2;

    public void setDatabaseEnum(DatabaseEnum database) {
        this.database = database;
    }
}
