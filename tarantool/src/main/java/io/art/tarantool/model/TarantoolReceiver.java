package io.art.tarantool.model;

import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import java.util.function.*;

@Getter
@AllArgsConstructor
public class TarantoolReceiver {
    private final IntegerValue id;
    private final Sinks.One<Value> sink;
    private final Consumer<ArrayValue> onChunk;
}
