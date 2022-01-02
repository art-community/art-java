package io.art.tarantool.test.model;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class User {
    private final int id;
    private final String name;
}
