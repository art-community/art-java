package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.tarantool.descriptor.*;
import lombok.*;
import org.msgpack.value.Value;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static lombok.AccessLevel.*;

@Public
@Getter(value = PACKAGE)
@RequiredArgsConstructor(access = PACKAGE)
public class TarantoolReactiveResponse {
    private final Mono<Value> output;
    private final MetaClass<? extends Space> space;

    public <T> Mono<T> parse() {
        return output.map(this::parse);
    }

    private <T> T parse(Value value) {
        TarantoolModelReader reader = tarantoolModule().configuration().getReader();
        return cast(reader.read(space.definition(), value));
    }
}
