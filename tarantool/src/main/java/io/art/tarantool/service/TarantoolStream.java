package io.art.tarantool.service;

import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.*;
import lombok.*;
import static io.art.core.collection.ImmutableArray.*;
import java.util.function.*;

@AllArgsConstructor
public class TarantoolStream<Type, Meta extends MetaClass<Type>> extends SpaceStream<Type, Meta> {
    private final TarantoolReactiveStream<Type, Meta> stream;

    @Override
    public SpaceStream<Type, Meta> limit(long value) {
        stream.limit(value);
        return this;
    }

    @Override
    public SpaceStream<Type, Meta> offset(long value) {
        stream.offset(value);
        return this;
    }

    @Override
    public SpaceStream<Type, Meta> range(long offset, long limit) {
        stream.range(offset, limit);
        return this;
    }

    @Override
    public SpaceStream<Type, Meta> distinct() {
        stream.distinct();
        return this;
    }

    @Override
    public <FieldType> SpaceStream<Type, Meta> sort(MetaField<Meta, FieldType> current, UnaryOperator<Sorter<Type, Meta, FieldType>> sorter) {
        stream.sort(current, sorter);
        return this;
    }

    @Override
    public <FieldType> SpaceStream<Type, Meta> filter(MetaField<Meta, FieldType> current, UnaryOperator<Filter<Type, Meta, FieldType>> filter) {
        stream.filter(current, filter);
        return this;
    }

    @Override
    public ImmutableArray<Type> collect() {
        return stream.collect().toStream().collect(immutableArrayCollector());
    }
}
