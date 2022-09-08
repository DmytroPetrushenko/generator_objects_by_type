package com.knubisoft;

import com.fasterxml.jackson.core.type.TypeReference;
import com.knubisoft.model.Person;
import com.knubisoft.service.MockGenerator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        MockGenerator mockGenerator = new MockGenerator(7, 8);

        TypeReference<String> stringTypeReference = new TypeReference<>() {};
        System.out.println(mockGenerator.startMockGenerator(stringTypeReference.getType())
                .toString());

        TypeReference<Integer> integerTypeReference = new TypeReference<>() {};
        System.out.println(mockGenerator.startMockGenerator(integerTypeReference.getType())
                .toString());

        TypeReference<List<String>> listTypeReference = new TypeReference<>() {};
        System.out.println(mockGenerator.startMockGenerator(listTypeReference.getType())
                .toString());
        TypeReference<Set<String>> setTypeReference = new TypeReference<>() {};
        System.out.println(mockGenerator.startMockGenerator(setTypeReference.getType())
                .toString());

        TypeReference<Queue<String>> queueTypeReference = new TypeReference<>() {};
        System.out.println(mockGenerator.startMockGenerator(queueTypeReference.getType())
                .toString());

        TypeReference<Map<String, List<Float>>> mapTypeReference = new TypeReference<>() {};
        System.out.println(mockGenerator.startMockGenerator(mapTypeReference.getType())
                .toString());

        TypeReference<Person> personTypeReference = new TypeReference<>() {};
        Person person = mockGenerator.startMockGenerator(personTypeReference.getType());
        System.out.println(person);

        TypeReference<Map<String, List<List<Person>>>> complexTypeReference
                = new TypeReference<>() {};
        System.out.println(mockGenerator.startMockGenerator(complexTypeReference.getType())
                .toString());
    }
}
