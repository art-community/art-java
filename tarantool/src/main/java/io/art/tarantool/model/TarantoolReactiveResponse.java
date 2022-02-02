package io.art.tarantool.model;

import io.art.core.annotation.*;
import io.art.tarantool.descriptor.*;
import lombok.*;
import org.msgpack.value.Value;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.meta.Meta.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static lombok.AccessLevel.*;

@Public
@Getter(value = PACKAGE)
@RequiredArgsConstructor(access = PACKAGE)
public class TarantoolReactiveResponse<T> {
    private final Mono<Value> output;
    private final Class<?> type;

    public Mono<T> parse() {
        return output.map(this::parse);
    }

    private T parse(Value value) {
        TarantoolModelReader reader = tarantoolModule().configuration().getReader();
        return cast(reader.read(definition(type), value));
    }
}
