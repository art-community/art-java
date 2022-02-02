package io.art.tarantool.test.model;

import lombok.*;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class TestData {
    int id;
    String content;
    Inner inner;

    @Value
    @Builder(toBuilder = true)
    @AllArgsConstructor
    public static class Inner {
        String content;
    }
}
