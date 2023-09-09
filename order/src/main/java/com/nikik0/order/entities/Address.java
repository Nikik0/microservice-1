package com.nikik0.order.entities;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Address {
    private final String Country;
    private final String City;
    private final String street;
    private final Integer building;
    private final Integer apartment;
}
