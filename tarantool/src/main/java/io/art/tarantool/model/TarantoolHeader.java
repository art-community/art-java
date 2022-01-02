package io.art.tarantool.model;

import lombok.*;

@Getter
@AllArgsConstructor
public class TarantoolHeader {
    private final long syncId;
    private final long code;
}
