package io.art.tarantool.model;

import lombok.*;
import org.msgpack.value.*;

@Getter
@AllArgsConstructor
public class TarantoolHeader {
    private final IntegerValue syncId;
    private final IntegerValue code;
}
