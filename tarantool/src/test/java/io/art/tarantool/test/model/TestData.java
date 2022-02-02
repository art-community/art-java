package io.art.tarantool.test.model;

import io.art.storage.*;
import lombok.*;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class TestData implements Space {
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
