package com.knubisoft.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneratorUtil {
    private static final List<String> ALPHABET = List.of("a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m");
    private static final int QUANTITY_SYMBOLS = 6;
    private static Map<Class<?>, Supplier<Object>> objectsSimpleMap;
    private static Map<Class<?>, Supplier<Object>> objectsCollectionsFrameworkMap;
    private final Random random = new Random();

    public GeneratorUtil() {
        if (objectsSimpleMap == null) {
            loadObjectsToMap();
        }
        if (objectsCollectionsFrameworkMap == null) {
            loadCollectionsFrameworkToMap();
        }
    }

    private void loadObjectsToMap() {
        objectsSimpleMap = new LinkedHashMap<>();
        objectsSimpleMap.put(String.class, this::getStringRandom);
        objectsSimpleMap.put(Integer.class, () -> random.nextInt(QUANTITY_SYMBOLS));
        objectsSimpleMap.put(Long.class, random::nextLong);
        objectsSimpleMap.put(Float.class, random::nextFloat);
        objectsSimpleMap.put(Double.class, random::nextDouble);
        objectsSimpleMap.put(Boolean.class, random::nextBoolean);
        objectsSimpleMap.put(Character.class, () -> ALPHABET.get(random.nextInt(ALPHABET.size())));
    }

    public Object getSimpleGeneratedObject(Class<?> clazz) {
        return objectsSimpleMap.get(clazz).get();
    }

    public Set<Class<?>> getSimpleClasses() {
        return objectsSimpleMap.keySet();
    }

    public Set<Class<?>> getCollectionClasses() {
        return objectsCollectionsFrameworkMap.keySet();
    }

    private String getStringRandom() {
        Supplier<String> stringSupplier = () -> ALPHABET.get(random.nextInt(ALPHABET.size()));
        return Stream.generate(stringSupplier)
                .limit(QUANTITY_SYMBOLS)
                .collect(Collectors.joining());
    }

    private void loadCollectionsFrameworkToMap() {
        objectsCollectionsFrameworkMap = new LinkedHashMap<>();
        objectsCollectionsFrameworkMap.put(List.class, ArrayList::new);
        objectsCollectionsFrameworkMap.put(Map.class, LinkedHashMap::new);
        objectsCollectionsFrameworkMap.put(Set.class, HashSet::new);
        objectsCollectionsFrameworkMap.put(Queue.class, ArrayDeque::new);
    }

    public Object getCollectionGeneratedObject(Class<?> clazz) {
        return objectsCollectionsFrameworkMap.get(clazz).get();
    }
}
