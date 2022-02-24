package io.art.storage;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.SpaceStream.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.PairFactory.*;
import static io.art.storage.SpaceStream.StreamOperation.*;
import java.util.*;
import java.util.function.*;

public abstract class ReactiveSpaceStream<Stream extends ReactiveSpaceStream<Stream, ?, ?>, Type, Meta extends MetaClass<Type>> {
    protected final List<Pair<StreamOperation, Object>> operators = linkedList();

    public Stream limit(int value) {
        operators.add(pairOf(LIMIT, value));
        return cast(this);
    }

    public Stream offset(int value) {
        operators.add(pairOf(OFFSET, value));
        return cast(this);
    }

    public Stream range(int offset, int limit) {
        return offset(offset).limit(limit);
    }

    public Stream distinct() {
        operators.add(pairOf(DISTINCT, null));
        return cast(this);
    }

    public <FieldType> Stream sort(MetaField<Meta, FieldType> current, UnaryOperator<SpaceStream.Sorter<Type, Meta, FieldType>> sorter) {
        operators.add(pairOf(SORT, sorter.apply(new SpaceStream.Sorter<>(current))));
        return cast(this);
    }

    public <FieldType> Stream filter(MetaField<Meta, FieldType> current, UnaryOperator<SpaceStream.Filter<Type, Meta, FieldType>> filter) {
        operators.add(pairOf(FILTER, filter.apply(new SpaceStream.Filter<>(current))));
        return cast(this);
    }

    public abstract Flux<Type> collect();
}
