package com.knubisoft.service;

import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class MockGenerator {
    public <T> T createGenerator(T object) {
        Class<T> clazz = (Class<T>) object.getClass();
        if (isCustomClass(clazz)) {
            return generateCustomClass(clazz);
        }
        if (isSimple(clazz)) {
            return generateSimpleClass(clazz);
        }
        return null;
    }



    private <T> boolean isSimple(Class<T> clazz) {
        return false;
    }

    private <T> boolean isCustomClass(Class<T> clazz) {
        return false;
    }

    @SneakyThrows
    private <T> T generateCustomClass(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return clazz.getConstructor().newInstance();
    }

    @SneakyThrows
    private <T> T generateSimpleClass(Class<T> clazz) {
        return clazz.getConstructor().newInstance();
    }
}
