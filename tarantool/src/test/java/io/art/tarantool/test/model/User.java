package io.art.tarantool.test.model;

import lombok.*;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class User {
    int id;
    String name;
    Address address;

    @Value
    @Builder(toBuilder = true)
    @AllArgsConstructor
    public static class Address {
        int house;
    }
}
