package com.knubisoft.model;

import lombok.Data;

import java.util.List;

@Data
public class Person {
    private String name;
    private String age;
    private List<Person> friends;
}
