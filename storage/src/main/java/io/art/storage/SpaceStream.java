package io.art.storage;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.StorageConstants.ProcessingOperation.*;
import java.util.*;
import java.util.function.*;

@Public
public abstract class SpaceStream<Type> {
    protected List<ProcessingOperator> operators = linkedList();

    public SpaceStream<Type> limit(long value) {
        operators.add(new ProcessingOperator(LIMIT, value));
        return this;
    }

    public SpaceStream<Type> offset(long value) {
        operators.add(new ProcessingOperator(OFFSET, value));
        return this;
    }

    public SpaceStream<Type> range(long offset, long limit) {
        return offset(offset).limit(limit);
    }

    public SpaceStream<Type> distinct() {
        operators.add(new ProcessingOperator(DISTINCT, null));
        return this;
    }

    public <FieldType> SpaceStream<Type> sort(MetaField<? extends MetaClass<Type>, FieldType> current, UnaryOperator<Sorter<Type, FieldType>> sorter) {
        operators.add(new ProcessingOperator(SORT, sorter.apply(new Sorter<>(current))));
        return this;
    }

    public SpaceStream<Type> filter(Consumer<Filter<Type>> filter) {
        Filter<Type> newFilter = new Filter<>();
        filter.accept(newFilter);
        operators.add(new ProcessingOperator(FILTER, newFilter));
        return this;
    }

    public SpaceStream<Type> refresh() {
        operators = linkedList();
        return this;
    }

    public abstract ImmutableArray<Type> collect();

    public abstract long count();

    public abstract boolean all(Consumer<Filter<Type>> filter);

    public abstract boolean any(Consumer<Filter<Type>> filter);
}
