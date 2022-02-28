package io.art.tarantool.service;

import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import java.util.function.*;

@AllArgsConstructor
public class TarantoolStream<Type> extends SpaceStream<Type> {
    private final TarantoolReactiveStream<Type> stream;

    @Override
    public SpaceStream<Type> limit(long value) {
        stream.limit(value);
        return this;
    }

    @Override
    public SpaceStream<Type> offset(long value) {
        stream.offset(value);
        return this;
    }

    @Override
    public SpaceStream<Type> range(long offset, long limit) {
        stream.range(offset, limit);
        return this;
    }

    @Override
    public SpaceStream<Type> distinct() {
        stream.distinct();
        return this;
    }

    @Override
    public <FieldType> SpaceStream<Type> sort(MetaField<? extends MetaClass<Type>, FieldType> current, UnaryOperator<Sorter<Type, FieldType>> sorter) {
        stream.sort(current, sorter);
        return this;
    }

    @Override
    public SpaceStream<Type> filter(Consumer<Filter<Type>> filter) {
        stream.filter(filter);
        return this;
    }

    @Override
    public SpaceStream<Type> refresh() {
        stream.refresh();
        return this;
    }

    @Override
    public ImmutableArray<Type> collect() {
        return stream.collect().toStream().collect(immutableArrayCollector());
    }

    @Override
    public long count() {
        return orElse(block(stream.count()), 0L);
    }

    @Override
    public boolean all(Consumer<Filter<Type>> filter) {
        return orElse(block(stream.all(filter)), false);
    }

    @Override
    public boolean any(Consumer<Filter<Type>> filter) {
        return orElse(block(stream.any(filter)), false);
    }
}
