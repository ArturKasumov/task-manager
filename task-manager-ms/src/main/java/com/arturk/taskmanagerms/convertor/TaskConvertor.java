package com.arturk.taskmanagerms.convertor;

import com.arturk.taskmanagerms.entity.TaskEntity;
import com.arturk.taskmanagerms.model.TaskDto;
import com.arturk.taskmanagerms.model.TaskRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskConvertor {

    void fromDTO(TaskRequest source, @MappingTarget TaskEntity destination);

    TaskDto toDTO(TaskEntity source);
}

