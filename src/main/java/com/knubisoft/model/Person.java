package com.knubisoft.model;

import java.util.List;
import lombok.Data;

@Data
public class Person {
    private String name;
    private String age;
    private Address address;
    private List<Person> friends;
}
