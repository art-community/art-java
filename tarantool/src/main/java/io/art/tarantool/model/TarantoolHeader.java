package io.art.tarantool.model;

import lombok.*;

@Getter
@AllArgsConstructor
public class TarantoolHeader {
    private final int syncId;
    private final long code;
}
