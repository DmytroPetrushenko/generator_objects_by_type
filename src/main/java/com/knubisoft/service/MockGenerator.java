package com.knubisoft.service;

import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.knubisoft.util.GeneratorUtil;
import com.knubisoft.util.TypeReferenceUtil;
import lombok.SneakyThrows;

public class MockGenerator {
    private static final int QUANTITY_ELEMENTS = 5;
    private final Random random = new Random();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final TypeReferenceUtil typeReferenceUtil = new TypeReferenceUtil();

    public <T> T createGenerator(Type typeName) {

        if (isCustomClass(typeName)) {
            return generateCustomObject(typeName);
        }
        if (isSimple(typeName)) {
            return generateSimpleObject(typeName);
        }
        if (isCollectionsFramework(typeName)) {
            return generateCollectionsFrameworkObject(typeName);
        }
        throw new RuntimeException("Can't define this class: " + typeName);
    }

    private boolean isCollectionsFramework(Type externalType) {
        List<TypeReference<?>> collectionTokens = typeReferenceUtil.getCollectionTokens();
        Optional<Type> optional = collectionTokens.stream()
                .map(TypeReference::getType)
                .filter(type -> getRawType(externalType).getTypeName().equals(type.getTypeName()))
                .findFirst();
        return optional.isPresent();
    }

    private Type getRawType(Type type) {
        return ((ParameterizedType) type).getRawType();
    }

    private boolean isSimple(Type externalType) {
        List<TypeReference<?>> simpleTokens = typeReferenceUtil.getSimpleTokens();
        Optional<Type> optional = simpleTokens.stream()
                .map(TypeReference::getType)
                .filter(type -> type.getTypeName().equals(externalType.getTypeName()))
                .findFirst();
        return optional.isPresent();
    }

    private boolean isCustomClass(Type externalType) {
        Optional<Type> optional = typeReferenceUtil.getCustomTokens().stream()
                .map(TypeReference::getType)
                .filter(type -> type.getTypeName().equals(externalType.getTypeName()))
                .findFirst();
        return optional.isPresent();
    }

    @SneakyThrows
    private <T> T generateCustomObject(Type externalType) {
        Class<T> clazz = (Class<T>) Class.forName(externalType.getTypeName());
        T instance = clazz.getConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(instance, createGenerator(field.getGenericType()));
        }
        return instance;
    }

    @SneakyThrows
    private <T> T generateSimpleObject(Type externalType) {
        Class<T> clazz = (Class<T>) Class.forName(externalType.getTypeName());
        return (T) generatorUtil.getGeneratedObject(clazz);
    }

    @SneakyThrows
    private <T> T generateCollectionsFrameworkObject(Type externalType) {
        ParameterizedType parameterizedType = (ParameterizedType) externalType;
        Type rawType = parameterizedType.getRawType();
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        Class<?> rawClass = Class.forName(rawType.getTypeName());

        if (checkIsList(rawClass)) {
            Type argument = typeArguments[0];
            List instance = (List) generatorUtil.getGeneratedObject(rawClass);
            Stream.generate(() -> createGenerator(argument))
                    .limit(QUANTITY_ELEMENTS)
                    .forEach(instance::add);
            return (T) instance;
        }

        if (checkIsMap(rawClass)) {

            List<Object> collect = Stream.of(typeArguments[0], typeArguments[1])
                    .map(this::createGenerator)
                    .collect(Collectors.toList());
            Map instance = (Map) generatorUtil.getGeneratedObject(rawClass);
            Stream.generate(() -> creatObjectForMap(typeArguments[0], typeArguments[1]))
                    .limit(QUANTITY_ELEMENTS)
                    .forEach(array -> instance.put(array[0], array[1]));
            return (T) instance;
        }
        throw new RuntimeException("Can't generate object from the class: "
                + externalType.getTypeName());
    }

    private Object[] creatObjectForMap(Type typeOne, Type typeTwo) {
        return new Object[]{createGenerator(typeOne), createGenerator(typeTwo)};
    }

    private boolean checkIsMap(Class<?> rawClass) {
        return rawClass.isAssignableFrom(Map.class);
    }

    private Boolean checkIsList(Class<?> rawClass) {
        return rawClass.isAssignableFrom(List.class);
    }
}
