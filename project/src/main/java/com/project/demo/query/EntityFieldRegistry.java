package com.project.demo.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EntityFieldRegistry {

    private final Map<Class<?>, Map<String, String>> cache = new ConcurrentHashMap<>();

    public String resolve(Class<?> entityType, String requestedName) {
        if (!StringUtils.hasText(requestedName)) {
            throw new InvalidQueryException("查询字段不能为空");
        }
        String normalized = requestedName.trim().replace("`", "");
        String column = fieldsFor(entityType).get(normalized);
        if (column == null) {
            throw new InvalidQueryException("不支持的查询字段: " + requestedName);
        }
        return column;
    }

    public Map<String, String> fieldsFor(Class<?> entityType) {
        return cache.computeIfAbsent(entityType, this::inspect);
    }

    private Map<String, String> inspect(Class<?> entityType) {
        Map<String, String> fields = new LinkedHashMap<>();
        Class<?> current = entityType;
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null && !tableField.exist()) {
                    continue;
                }
                String column = columnName(field, tableField).replace("`", "");
                if (!column.matches("[A-Za-z][A-Za-z0-9_]*")) {
                    throw new IllegalStateException("Unsafe mapped column on "
                            + entityType.getSimpleName() + "." + field.getName());
                }
                fields.put(field.getName(), column);
                fields.put(column, column);
            }
            current = current.getSuperclass();
        }
        return Collections.unmodifiableMap(fields);
    }

    private String columnName(Field field, TableField tableField) {
        TableId tableId = field.getAnnotation(TableId.class);
        if (tableId != null && StringUtils.hasText(tableId.value())) {
            return tableId.value();
        }
        if (tableField != null && StringUtils.hasText(tableField.value())) {
            return tableField.value();
        }
        return camelToSnake(field.getName());
    }

    private String camelToSnake(String value) {
        return value.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
    }
}
