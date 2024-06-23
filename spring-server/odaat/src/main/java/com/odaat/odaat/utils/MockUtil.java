package com.odaat.odaat.utils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.scheduling.config.Task;

import com.odaat.odaat.dto.request.CategoryRequest;
import com.odaat.odaat.dto.request.ProjectRequest;
import com.odaat.odaat.dto.request.TaskRequest;
import com.odaat.odaat.dto.request.UzerNameUpdateRequest;
import com.odaat.odaat.dto.response.CategoryResponse;
import com.odaat.odaat.dto.response.ProjectResponse;
import com.odaat.odaat.dto.response.TaskResponse;
import com.odaat.odaat.dto.response.UzerResponse;
import com.odaat.odaat.model.Category;
import com.odaat.odaat.model.Project;
import com.odaat.odaat.model.Uzer;

/* Create mock objects for testing */
public class MockUtil {

    public static <T> T mockInstance(Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object mockValue = getMockValue(field.getType());
                field.set(instance, mockValue);
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create mock instance of " + clazz.getName(), e);
        }
    }

    private static Object getMockValue(Class<?> type) {
        Random random = new Random();
        if (type.equals(String.class)) {
            return "mock_" + UUID.randomUUID().toString().substring(0, 8);
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return random.nextInt(100);
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return random.nextLong();
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            return random.nextDouble();
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return random.nextBoolean();
        } else if (type.equals(LocalDate.class)) {
            return LocalDate.ofEpochDay(ThreadLocalRandom.current().nextInt(0, 365 * 50));
        } else if (type.equals(LocalDateTime.class)) {
            return LocalDateTime.now().minusDays(random.nextInt(365));
        } else if (type.isEnum()) {
            Object[] enumValues = type.getEnumConstants();
            return enumValues[random.nextInt(enumValues.length)];
        } else if (Collection.class.isAssignableFrom(type)) {
            return new ArrayList<>();
        } else if (Map.class.isAssignableFrom(type)) {
            return new HashMap<>();
        } else if (type.equals(Task.class) || type.equals(Project.class) || type.equals(Category.class) || type.equals(Uzer.class) || type.equals(TaskResponse.class) || type.equals(ProjectResponse.class) || type.equals(CategoryResponse.class) || type.equals(UzerResponse.class) || type.equals(TaskRequest.class) || type.equals(ProjectRequest.class) || type.equals(CategoryRequest.class) || type.equals(UzerNameUpdateRequest.class)  ) {
            return mockInstance(type);
        }
        return null;
    }
}