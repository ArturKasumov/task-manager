package com.arturk.taskmanagerms.bdd;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TestContext {

    @Setter(AccessLevel.NONE)
    private Map<String, Long> taskEntityCache = new HashMap<>();

    @Setter(AccessLevel.PRIVATE)
    private Object responseObject;

    @Setter(AccessLevel.PRIVATE)
    private Class responseClass;

    public void setResponseObject(Object responseObject, Class<?> responseClass) {
        setResponseObject(responseObject);
        setResponseClass(responseClass);
    }

}
