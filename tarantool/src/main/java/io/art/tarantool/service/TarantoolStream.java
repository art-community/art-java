package io.art.tarantool.service;

import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.filter.model.*;
import io.art.storage.sorter.model.*;
import io.art.storage.stream.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class TarantoolStream<Type> extends SpaceStream<Type> {
    private final TarantoolReactiveStream<Type> stream;

    public TarantoolStream(MetaType<Type> returningType, TarantoolReactiveStream<Type> stream) {
        super(returningType);
        this.stream = stream;
    }

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
    public SpaceStream<Type> distinct(MetaField<? extends MetaClass<Type>, ?> field) {
        stream.distinct(field);
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
    public <Mapped> Stream<Mapped> map(MetaField<? extends MetaClass<Type>, Mapped> field) {
        return stream.map(field).toStream();
    }

    @Override
    public <Mapped> Stream<Mapped> map(MetaMethod<? extends MetaClass<? extends Storage>, Mapped> function) {
        return stream.map(function).toStream();
    }

    @Override
    public <Mapped> SpaceStream<Mapped> map(MetaClass<Mapped> space, MetaField<? extends MetaClass<Type>, ?> field) {
        stream.map(space, field);
        return cast(this);
    }

    @Override
    public <Mapped> SpaceStream<Mapped> map(MetaClass<Mapped> space, MetaField<? extends MetaClass<Type>, ?>... indexedFields) {
        stream.map(space, indexedFields);
        return cast(this);
    }

    @Override
    public <FieldType> Optional<Type> max(MetaField<? extends MetaClass<Type>, FieldType> byField) {
        return stream.max(byField).blockOptional();
    }

    @Override
    public <FieldType> Optional<Type> min(MetaField<? extends MetaClass<Type>, FieldType> byField) {
        return stream.min(byField).blockOptional();
    }

    @Override
    public Optional<Type> first() {
        return stream.first().blockOptional();
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

    @Override
    public boolean none(Consumer<Filter<Type>> filter) {
        return orElse(block(stream.none(filter)), false);
    }
}
