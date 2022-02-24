package io.art.tarantool.service;

import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.*;
import lombok.*;
import static io.art.core.collection.ImmutableArray.*;

@AllArgsConstructor
public class TarantoolStream<Type, Meta extends MetaClass<Type>> extends SpaceStream<TarantoolStream<Type, Meta>, Type, Meta> {
    private final TarantoolReactiveStream<Type, Meta> stream;

    @Override
    public ImmutableArray<Type> collect() {
        return stream.collect().toStream().collect(immutableArrayCollector());
    }
}
