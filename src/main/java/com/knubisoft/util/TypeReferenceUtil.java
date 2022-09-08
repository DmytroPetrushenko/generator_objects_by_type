package com.knubisoft.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.knubisoft.model.Address;
import com.knubisoft.model.Person;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.SneakyThrows;

public class TypeReferenceUtil {
    private static List<TypeReference<?>> simpleTokens;
    private static List<TypeReference<?>> customTokens;
    private static List<TypeReference<?>> collectionTokens;

    public List<TypeReference<?>> getCustomTokens() {
        if (customTokens == null) {
            createCustomTokens();
        }
        return customTokens;
    }

    public List<TypeReference<?>> getSimpleTokens() {
        if (simpleTokens == null) {
            createSimpleTokens();
        }
        return simpleTokens;
    }

    public List<TypeReference<?>> getCollectionTokens() {
        if (collectionTokens == null) {
            createCollectionTokens();
        }
        return collectionTokens;
    }

    private void createCollectionTokens() {
        TypeReference<List> listToken = new TypeReference<>() {};
        TypeReference<Map> mapToken = new TypeReference<>() {};
        collectionTokens = List.of(listToken, mapToken);
    }

    private void createCustomTokens() {
        TypeReference<Person> tokenPerson = new TypeReference<>() {};
        TypeReference<Address> tokenAddress = new TypeReference<>() {};
        customTokens = List.of(tokenPerson, tokenAddress);
    }

    private void createSimpleTokens() {
        TypeReference<String> tokenString = new TypeReference<>() {};
        TypeReference<Integer> tokenInteger = new TypeReference<>() {};
        TypeReference<Float> tokenFloat = new TypeReference<>() {};
        TypeReference<Boolean> tokenBoolean = new TypeReference<>() {};
        simpleTokens = List.of(tokenString, tokenInteger, tokenBoolean,tokenFloat);
    }



}
