package io.art.tarantool.model;

import lombok.*;

@Getter
@AllArgsConstructor
public class TarantoolCredentials {
    private final String username;
    private final String password;
}
