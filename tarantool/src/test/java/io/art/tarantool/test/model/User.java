package io.art.tarantool.test.model;

import io.art.storage.*;
import lombok.*;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class User implements Space {
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
