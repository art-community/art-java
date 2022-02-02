package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.tarantool.descriptor.*;
import lombok.*;
import org.msgpack.value.Value;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static lombok.AccessLevel.*;

@Public
@Getter(value = PACKAGE)
public class TarantoolDefaultResponse {
    private final Mono<Value> output;
    private final TarantoolReactiveResponse reactive;
    private final MetaClass<? extends Space> space;

    TarantoolDefaultResponse(Mono<Value> output, MetaClass<? extends Space> space) {
        this.output = output;
        this.space = space;
        reactive = new TarantoolReactiveResponse(output, space);
    }

    public <T> T get() {
        return parse(block(output));
    }

    public TarantoolReactiveResponse reactive() {
        return reactive;
    }

    private <T> T parse(Value value) {
        TarantoolModelReader reader = tarantoolModule().configuration().getReader();
        return cast(reader.read(space.definition(), value));
    }
}
