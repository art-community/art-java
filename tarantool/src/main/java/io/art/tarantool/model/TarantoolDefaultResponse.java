package io.art.tarantool.model;

import io.art.core.annotation.*;
import io.art.tarantool.descriptor.*;
import lombok.*;
import org.msgpack.value.Value;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.meta.Meta.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static lombok.AccessLevel.*;

@Public
@Getter(value = PACKAGE)
public class TarantoolDefaultResponse<T> {
    private final Mono<Value> output;
    private final TarantoolReactiveResponse<T> reactive;
    private final Class<?> type;

    public TarantoolDefaultResponse(Mono<Value> output, Class<?> type) {
        this.output = output;
        this.type = type;
        reactive = new TarantoolReactiveResponse<T>(output, type);
    }

    public T get() {
        return parse(block(output));
    }

    public TarantoolReactiveResponse<T> reactive() {
        return reactive;
    }

    private T parse(Value value) {
        TarantoolModelReader reader = tarantoolModule().configuration().getReader();
        return cast(reader.read(definition(type), value));
    }
}
