package io.art.storage;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.SpaceStream.*;
import reactor.core.publisher.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.PairFactory.*;
import static io.art.storage.SpaceStream.StreamOperation.*;
import java.util.*;
import java.util.function.*;

public abstract class ReactiveSpaceStream<Type, Meta extends MetaClass<Type>> {
    protected final List<Pair<StreamOperation, Object>> operators = linkedList();

    public ReactiveSpaceStream<Type, Meta> limit(long value) {
        operators.add(pairOf(LIMIT, value));
        return this;
    }

    public ReactiveSpaceStream<Type, Meta> offset(long value) {
        operators.add(pairOf(OFFSET, value));
        return this;
    }

    public ReactiveSpaceStream<Type, Meta> range(long offset, long limit) {
        return offset(offset).limit(limit);
    }

    public ReactiveSpaceStream<Type, Meta> distinct() {
        operators.add(pairOf(DISTINCT, null));
        return this;
    }

    public <FieldType> ReactiveSpaceStream<Type, Meta> sort(MetaField<Meta, FieldType> current, UnaryOperator<SpaceStream.Sorter<Type, Meta, FieldType>> sorter) {
        operators.add(pairOf(SORT, sorter.apply(new SpaceStream.Sorter<>(current))));
        return this;
    }

    public ReactiveSpaceStream<Type, Meta> filter(UnaryOperator<SpaceStream.Filter<Type, Meta>> filter) {
        operators.add(pairOf(FILTER, filter.apply(new SpaceStream.Filter<>())));
        return this;
    }

    public abstract Flux<Type> collect();
}
