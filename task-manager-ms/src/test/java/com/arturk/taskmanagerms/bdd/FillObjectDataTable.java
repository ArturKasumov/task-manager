package com.arturk.taskmanagerms.bdd;

import io.cucumber.datatable.DataTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class FillObjectDataTable {

    private int fieldIndex = -1;

    private int valueIndex = -1;

    public FillObjectDataTable() {
        fieldIndex = 0;
        valueIndex = 1;
    }

    public FillObjectDataTable(List<String> header) {
        fieldIndex = header.indexOf("Field");
        valueIndex = header.indexOf("Value");
    }

    public FillObjectDataTableRow parseRow(List<String> rowData) {
        FillObjectDataTableRow row = new FillObjectDataTableRow();
        row.setField(rowData.get(fieldIndex));
        row.setValue(rowData.get(valueIndex));
        return row;
    }

    public void fillObject(DataTable datatable, Object request) {
        List<List<String>> dataTableData = datatable.asLists();
        FillObjectDataTable processor = new FillObjectDataTable(dataTableData.get(0));
        for (int i = 1; i < dataTableData.size(); i++) {
            FillObjectDataTableRow row = processor.parseRow(dataTableData.get(i));
            Iterator<String> iterator = Arrays.asList(row.getField().split("\\.")).iterator();
            Object current = request;

            // logic for initializing arrays
            if (row.getField().matches(".*\\[\\d+\\].*")) {
                try {
                    while (iterator.hasNext()) {
                        String fieldName = iterator.next();
                        // creating array if needed and getting n-th element of array as current object
                        if (fieldName.matches(".*\\[\\d+\\]")) {
                            String arrayFieldName = fieldName.substring(0, fieldName.indexOf("["));
                            int index = Integer.parseInt(fieldName.substring(fieldName.indexOf("[") + 1, fieldName.indexOf("]")));

                            List<Object> list = (List<Object>) ReflectionTestUtils.getField(current, arrayFieldName);
                            if (list == null) {
                                list = new ArrayList<>();
                                ReflectionTestUtils.setField(current, arrayFieldName, list);
                            }

                            Field arrayField = ReflectionUtils.findField(current.getClass(), arrayFieldName);
                            if (arrayField == null) {
                                throw new IllegalArgumentException("No list field found. Fieldname: " + arrayFieldName);
                            }

                            ParameterizedType arrayType = (ParameterizedType) arrayField.getGenericType();
                            Class<?> arrayElementType = (Class<?>) arrayType.getActualTypeArguments()[0];

                            while (list.size() <= index) {
                                list.add(arrayElementType.newInstance());
                            }
                            current = list.get(index);
                        } else {  // setting field of current object (element of array)
                            setFieldValue(current, fieldName, row);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                // logic for initializing non arrays
                try {
                    while (iterator.hasNext()) {
                        String fieldName = iterator.next();
                        if (!iterator.hasNext()) { // field of current object is simple type
                            setFieldValue(current, fieldName, row);
                        } else { // field of current object is other object
                            Object fieldValue = ReflectionTestUtils.getField(current, fieldName);
                            if (fieldValue == null) {
                                Field field = ReflectionUtils.findField(current.getClass(), fieldName);
                                fieldValue = field.getType().newInstance();
                                ReflectionTestUtils.setField(current, fieldName, fieldValue);
                            }
                            current = fieldValue;
                        }
                    }
                } catch (Exception e) {
                    log.error("Could not instantiate '" + row.getValue() + "' value of '" + row.getField() + "' field", e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void setFieldValue(Object currentObject, String fieldName, FillObjectDataTableRow dataTableRow) throws InvocationTargetException, IllegalAccessException {
        Field field = ReflectionUtils.findField(currentObject.getClass(), fieldName);
        if (field == null) {
            throw new IllegalArgumentException("No field found. Fieldname: " + fieldName);
        }
        Object value;
        if (field.getType().equals(String.class) && "\"\"".equals(dataTableRow.getValue())) {
            value = StringUtils.EMPTY;
        } else if (field.getType().equals(String.class)) {
            value = dataTableRow.getValue();
        } else if (field.getType().equals(Boolean.class)) {
            if (dataTableRow.getValue() == null) {
                value = null;
            } else {
                value = Boolean.valueOf(dataTableRow.getValue());
            }
        } else if (field.getType().equals(boolean.class)) {
            value = Boolean.valueOf(dataTableRow.getValue()).booleanValue();
        } else if (field.getType().equals(Character.class)) {
            value = dataTableRow.getValue().charAt(0);
        } else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
            value = dataTableRow.getValue() == null ? null : Integer.valueOf(dataTableRow.getValue());
        } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
            value = dataTableRow.getValue() == null ? null : Long.valueOf(dataTableRow.getValue());
        } else if (field.getType().equals(LocalDate.class)) {
            if (dataTableRow.getValue() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                value = LocalDate.parse(dataTableRow.getValue(), formatter);
            } else {
                value = null;
            }
        } else if (field.getType().equals(LocalDateTime.class)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            value = LocalDateTime.parse(dataTableRow.getValue(), formatter);
        } else if (field.getType().isEnum()) {
            Method fromValue = ReflectionUtils.findMethod(field.getType(), "fromValue", String.class);
            if (!Objects.isNull(fromValue)) {
                value = fromValue.invoke(null, dataTableRow.getValue());
            } else {
                Method valueOf = ReflectionUtils.findMethod(field.getType(), "valueOf", String.class);
                value = valueOf.invoke(null, dataTableRow.getValue());
            }
        } else if (List.class.isAssignableFrom(field.getType()) && ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(String.class)) {
            value = Arrays.stream(dataTableRow.getValue().split(";")).map(String::trim).collect(Collectors.toList());
        } else {
            throw new UnsupportedOperationException("Could not convert to " + field.getType() + ". Field: " + dataTableRow.getField());
        }
        ReflectionTestUtils.setField(currentObject, fieldName, value);
    }
}

