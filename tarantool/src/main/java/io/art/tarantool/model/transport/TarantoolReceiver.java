package io.art.tarantool.model.transport;

import lombok.*;
import org.msgpack.value.Value;
import reactor.core.publisher.*;

@Getter
@AllArgsConstructor
public class TarantoolReceiver {
    private final int id;
    private final Sinks.One<Value> sink;
}
