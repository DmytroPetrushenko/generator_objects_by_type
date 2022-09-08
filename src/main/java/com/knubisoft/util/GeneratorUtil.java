package com.knubisoft.util;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneratorUtil {
    private final static List<String> ALPHABET = List.of("a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m");
    private static final int QUANTITY_SYMBOLS = 6;
    private static Map<Class<?>, Supplier<Object>> objectsMap;
    private final Random random = new Random();

    public GeneratorUtil() {
        if (objectsMap == null) {
            loadObjectsToMap();
        }
    }

    private void loadObjectsToMap() {
        objectsMap = new LinkedHashMap<>();
        objectsMap.put(String.class, this::getStringRandom);
        objectsMap.put(Integer.class, () -> random.nextInt(QUANTITY_SYMBOLS));
        objectsMap.put(Long.class, () -> random.nextLong(QUANTITY_SYMBOLS));
        objectsMap.put(Float.class, () -> random.nextFloat(QUANTITY_SYMBOLS));
        objectsMap.put(Double.class, () -> random.nextDouble(QUANTITY_SYMBOLS));
        objectsMap.put(Boolean.class, random::nextBoolean);
        objectsMap.put(Character.class, () -> ALPHABET.get(random.nextInt(ALPHABET.size())));
        objectsMap.put(List.class, ArrayList::new);
        objectsMap.put(Map.class, LinkedHashMap::new);
    }

    public Object getGeneratedObject(Class<?> clazz) {
        return objectsMap.get(clazz).get();
    }

    private String getStringRandom() {
        Supplier<String> stringSupplier = () -> ALPHABET.get(random.nextInt(ALPHABET.size()));
        return Stream.generate(stringSupplier)
                .limit(QUANTITY_SYMBOLS)
                .collect(Collectors.joining());
    }
}
