package com.knubisoft.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import lombok.Getter;

@Getter
public class GeneratorUtil {
    private final Map<Class<?>, Supplier<Object>> simpleMap = new LinkedHashMap<>();

    public GeneratorUtil() {
        fillSimpleMap();
    }

    private void fillSimpleMap() {
        simpleMap.put(String.class, () -> "generated");
        simpleMap.put(Integer.class, () -> 1234);
        simpleMap.put(Long.class, () -> 12345L);
        simpleMap.put(Float.class, () -> 123.45);
        simpleMap.put(Double.class, () -> 123.456);
        simpleMap.put(Byte.class, () -> 127);
        simpleMap.put(Boolean.class, () -> true);
        simpleMap.put(Character.class, () -> "a" );
    }
}
