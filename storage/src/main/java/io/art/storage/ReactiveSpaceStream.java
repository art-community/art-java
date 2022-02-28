package io.art.storage;

import io.art.meta.model.*;
import reactor.core.publisher.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.SpaceStream.*;
import static io.art.storage.SpaceStream.ProcessingOperation.*;
import java.util.*;
import java.util.function.*;

public abstract class ReactiveSpaceStream<Type> {
    protected List<ProcessingOperator> operators = linkedList();

    public ReactiveSpaceStream<Type> limit(long value) {
        operators.add(new ProcessingOperator(LIMIT, value));
        return this;
    }

    public ReactiveSpaceStream<Type> offset(long value) {
        operators.add(new ProcessingOperator(OFFSET, value));
        return this;
    }

    public ReactiveSpaceStream<Type> range(long offset, long limit) {
        return offset(offset).limit(limit);
    }

    public ReactiveSpaceStream<Type> distinct() {
        operators.add(new ProcessingOperator(DISTINCT, null));
        return this;
    }

    public <FieldType> ReactiveSpaceStream<Type> sort(MetaField<? extends MetaClass<Type>, FieldType> current, UnaryOperator<SpaceStream.Sorter<Type, FieldType>> sorter) {
        operators.add(new ProcessingOperator(SORT, sorter.apply(new SpaceStream.Sorter<>(current))));
        return this;
    }

    public ReactiveSpaceStream<Type> filter(Consumer<Filter<Type>> filter) {
        Filter<Type> newFilter = new Filter<>();
        filter.accept(newFilter);
        operators.add(new ProcessingOperator(FILTER, newFilter));
        return this;
    }

    public ReactiveSpaceStream<Type> refresh() {
        operators = linkedList();
        return this;
    }

    public abstract Flux<Type> collect();
}
