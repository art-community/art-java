package io.art.tarantool.test.model;

import lombok.*;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class OtherSpace {
    int key;
    String value;
    int number;
}
