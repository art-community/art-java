package io.art.tarantool.stream;

import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.filter.model.*;
import io.art.storage.index.*;
import io.art.storage.sorter.model.*;
import io.art.storage.stream.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@SuppressWarnings({VARARGS})
public class TarantoolBlockingRouterSpaceStream<Type> extends BlockingSpaceStream<Type> {
    private final ReactiveSpaceStream<Type> stream;

    public TarantoolBlockingRouterSpaceStream(MetaType<Type> returningType, ReactiveSpaceStream<Type> stream) {
        super(returningType);
        this.stream = stream;
    }

    @Override
    public BlockingSpaceStream<Type> limit(long value) {
        stream.limit(value);
        return this;
    }

    @Override
    public BlockingSpaceStream<Type> offset(long value) {
        stream.offset(value);
        return this;
    }

    @Override
    public BlockingSpaceStream<Type> range(long offset, long limit) {
        stream.range(offset, limit);
        return this;
    }

    @Override
    public BlockingSpaceStream<Type> distinct(MetaField<? extends MetaClass<Type>, ?> field) {
        stream.distinct(field);
        return this;
    }

    @Override
    public <FieldType> BlockingSpaceStream<Type> sort(MetaField<? extends MetaClass<Type>, FieldType> current, UnaryOperator<Sorter<Type, FieldType>> sorter) {
        stream.sort(current, sorter);
        return this;
    }

    @Override
    public BlockingSpaceStream<Type> filter(Consumer<Filter<Type>> filter) {
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
    public <Mapped> BlockingSpaceStream<Mapped> map(MetaClass<Mapped> space, MetaField<? extends MetaClass<Type>, ?> field) {
        stream.map(space, field);
        return cast(this);
    }

    @Override
    public <Mapped, F1> BlockingSpaceStream<Mapped> map(Index1<Mapped, F1> index, MetaField<? extends MetaClass<Type>, F1> field1) {
        stream.map(index, field1);
        return cast(this);
    }

    @Override
    public <Mapped, F1, F2> BlockingSpaceStream<Mapped> map(Index2<Mapped, F1, F2> index,
                                                            MetaField<? extends MetaClass<Type>, F1> field1,
                                                            MetaField<? extends MetaClass<Type>, F2> field2) {
        stream.map(index, field1, field2);
        return cast(this);
    }

    @Override
    public <Mapped, F1, F2, F3> BlockingSpaceStream<Mapped> map(Index3<Mapped, F1, F2, F3> index,
                                                                MetaField<? extends MetaClass<Type>, F1> field1,
                                                                MetaField<? extends MetaClass<Type>, F2> field2,
                                                                MetaField<? extends MetaClass<Type>, F3> field3) {
        stream.map(index, field1, field2, field3);
        return cast(this);
    }

    @Override
    public <Mapped, F1, F2, F3, F4> BlockingSpaceStream<Mapped> map(Index4<Mapped, F1, F2, F3, F4> index,
                                                                    MetaField<? extends MetaClass<Type>, F1> field1,
                                                                    MetaField<? extends MetaClass<Type>, F2> field2,
                                                                    MetaField<? extends MetaClass<Type>, F3> field3,
                                                                    MetaField<? extends MetaClass<Type>, F4> field4) {
        stream.map(index, field1, field2, field3, field4);
        return cast(this);
    }

    @Override
    public <Mapped, F1, F2, F3, F4, F5> BlockingSpaceStream<Mapped> map(Index5<Mapped, F1, F2, F3, F4, F5> index,
                                                                        MetaField<? extends MetaClass<Type>, F1> field1,
                                                                        MetaField<? extends MetaClass<Type>, F2> field2,
                                                                        MetaField<? extends MetaClass<Type>, F3> field3,
                                                                        MetaField<? extends MetaClass<Type>, F4> field4,
                                                                        MetaField<? extends MetaClass<Type>, F5> field5) {
        stream.map(index, field1, field2, field3, field4, field5);
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
