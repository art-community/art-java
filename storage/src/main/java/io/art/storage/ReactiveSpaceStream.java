package io.art.storage;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import reactor.core.publisher.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.StorageConstants.ProcessingOperation.*;
import java.util.*;
import java.util.function.*;

@Public
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

    public <FieldType> ReactiveSpaceStream<Type> sort(MetaField<? extends MetaClass<Type>, FieldType> current, UnaryOperator<Sorter<Type, FieldType>> sorter) {
        operators.add(new ProcessingOperator(SORT, sorter.apply(new Sorter<>(current))));
        return this;
    }

    public ReactiveSpaceStream<Type> filter(Consumer<Filter<Type>> filter) {
        Filter<Type> newFilter = new Filter<>();
        filter.accept(newFilter);
        operators.add(new ProcessingOperator(FILTER, newFilter));
        return this;
    }

    public <Mapped> ReactiveSpaceStream<Type> map(MetaField<? extends MetaClass<Type>, Mapped> field) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byField(field)));
        return this;
    }

    public <Mapped> ReactiveSpaceStream<Type> map(MetaClass<Mapped> space, MetaField<? extends MetaClass<Type>, ?> field) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().bySpace(space, field)));
        return this;
    }

    public <Mapped> ReactiveSpaceStream<Type> map(MetaClass<Mapped> space, MetaField<? extends MetaClass<Type>, ?>... indexedFields) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byIndex(space, indexedFields)));
        return this;
    }

    public <Mapped> ReactiveSpaceStream<Type> map(MetaMethod<MetaClass<? extends Storage>, Mapped> function) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byFunction(function)));
        return this;
    }

    public ReactiveSpaceStream<Type> refresh() {
        operators = linkedList();
        return this;
    }

    public abstract Flux<Type> collect();

    public abstract Mono<Long> count();

    public abstract Mono<Boolean> all(Consumer<Filter<Type>> filter);

    public abstract Mono<Boolean> any(Consumer<Filter<Type>> filter);
}
