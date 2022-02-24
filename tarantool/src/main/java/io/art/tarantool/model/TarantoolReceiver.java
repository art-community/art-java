package io.art.tarantool.model;

import lombok.*;
import org.msgpack.value.*;
import org.msgpack.value.Value;
import reactor.core.publisher.*;

@Getter
@AllArgsConstructor
public class TarantoolReceiver {
    private final IntegerValue id;
    private final Sinks.One<Value> sink;
}
