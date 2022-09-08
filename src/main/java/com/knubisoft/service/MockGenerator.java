package com.knubisoft.service;

import com.knubisoft.util.GeneratorUtil;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;

public class MockGenerator {
    private static final int LIMIT_RECURSION = 6;
    private static final int QUANTITY_ELEMENTS = 5;
    private final Random random = new Random();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    public <T> T startMockGenerator(Type typeName) {
         int count = 0;
         return createGenerator(typeName, count);
    }

    private  <T> T createGenerator(Type typeName, int count) {
        if (isSimple(typeName)) {
            return generateSimpleObject(typeName);
        }
        if ((count = count + 1) == LIMIT_RECURSION) {
            return null;
        }
        if (isCollectionsFramework(typeName)) {
            return generateCollectionsFrameworkObject(typeName, count);
        }
        return generateCustomObject(typeName, count);
    }
    private boolean isSimple(Type externalType) {
        Set<Class<?>> simpleClasses = generatorUtil.getSimpleClasses();
        Optional<String> optional = simpleClasses.stream()
                .map(Class::getTypeName)
                .filter(typeName -> typeName.equals(externalType.getTypeName()))
                .findFirst();
        return optional.isPresent();
    }

    private boolean isCollectionsFramework(Type externalType) {
        Set<Class<?>> collectionClasses = generatorUtil.getCollectionClasses();
        Optional<String> optional = collectionClasses.stream()
                .map(Class::getTypeName)
                .filter(typeName -> typeName.equals(getRawType(externalType).getTypeName()))
                .findFirst();
        return optional.isPresent();
    }

    private Type getRawType(Type type) {
        try {
        return ((ParameterizedType) type).getRawType();
        } catch (ClassCastException e) {
            return type;
        }
    }

    @SneakyThrows
    private <T> T generateSimpleObject(Type externalType) {
        Class<T> clazz = (Class<T>) Class.forName(externalType.getTypeName());
        return (T) generatorUtil.getSimpleGeneratedObject(clazz);
    }

    @SneakyThrows
    private <T> T generateCollectionsFrameworkObject(Type externalType, int count) {
        ParameterizedType parameterizedType = (ParameterizedType) externalType;
        Type rawType = parameterizedType.getRawType();
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        Class<?> rawClass = Class.forName(rawType.getTypeName());

        if (checkIsList(rawClass)) {
            Type argument = typeArguments[0];
            List instance = (List) generatorUtil.getCollectionGeneratedObject(rawClass);
            Stream.generate(() -> createGenerator(argument, count))
                    .limit(QUANTITY_ELEMENTS)
                    .forEach(instance::add);
            return (T) instance;
        }

        if (checkIsMap(rawClass)) {
            List<Object> collect = Stream.of(typeArguments[0], typeArguments[1])
                    .map(type -> createGenerator(type, count))
                    .collect(Collectors.toList());
            Map instance = (Map) generatorUtil.getCollectionGeneratedObject(rawClass);
            Stream.generate(() -> creatObjectForMap(typeArguments[0], typeArguments[1], count))
                    .limit(QUANTITY_ELEMENTS)
                    .forEach(array -> instance.put(array[0], array[1]));
            return (T) instance;
        }
        throw new RuntimeException("Can't generate object from the class: "
                + externalType.getTypeName());
    }

    private Object[] creatObjectForMap(Type typeOne, Type typeTwo, int count) {
        return new Object[]{createGenerator(typeOne, count), createGenerator(typeTwo, count)};
    }

    private boolean checkIsMap(Class<?> rawClass) {
        return rawClass.isAssignableFrom(Map.class);
    }

    private boolean checkIsList(Class<?> rawClass) {
        return rawClass.isAssignableFrom(List.class);
    }

    @SneakyThrows
    private <T> T generateCustomObject(Type externalType, int count) {
        Class<T> clazz = (Class<T>) Class.forName(externalType.getTypeName());
        T instance = clazz.getConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(instance, createGenerator(field.getGenericType(), count));
        }
        return instance;
    }
}
