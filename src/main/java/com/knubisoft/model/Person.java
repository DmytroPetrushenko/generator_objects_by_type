package com.knubisoft.model;

import lombok.Data;

@Data
public class Person {
    private String name;
    private String age;
    private Address address;
}
