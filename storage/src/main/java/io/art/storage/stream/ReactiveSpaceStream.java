package io.art.storage.stream;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.filter.implementation.*;
import io.art.storage.filter.model.*;
import io.art.storage.mapper.*;
import io.art.storage.sorter.implementation.*;
import io.art.storage.sorter.model.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.constants.StorageConstants.FilterCondition.*;
import static io.art.storage.constants.StorageConstants.ProcessingOperation.*;
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

    public ReactiveSpaceStream<Type> distinct(MetaField<? extends MetaClass<Type>, ?> field) {
        operators.add(new ProcessingOperator(DISTINCT, field));
        return this;
    }

    public <FieldType> ReactiveSpaceStream<Type> sort(MetaField<? extends MetaClass<Type>, FieldType> current, UnaryOperator<Sorter<Type, FieldType>> sorter) {
        operators.add(new ProcessingOperator(SORT, sorter.apply(new SorterImplementation<>(current))));
        return this;
    }

    public <FieldType> Mono<Type> max(MetaField<? extends MetaClass<Type>, FieldType> byField) {
        return sort(byField, Sorter::descendant).first();
    }

    public <FieldType> Mono<Type> min(MetaField<? extends MetaClass<Type>, FieldType> byField) {
        return sort(byField, Sorter::ascendant).first();
    }

    public ReactiveSpaceStream<Type> filter(Consumer<Filter<Type>> filter) {
        FilterImplementation<Type> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        operators.add(new ProcessingOperator(FILTER, newFilter));
        return this;
    }

    public <Mapped> ReactiveSpaceStream<Mapped> map(MetaClass<Mapped> space, MetaField<? extends MetaClass<Type>, ?> field) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().bySpace(space, field)));
        return cast(this);
    }

    public <Mapped> ReactiveSpaceStream<Mapped> map(MetaClass<Mapped> space, MetaField<? extends MetaClass<Type>, ?>... indexedFields) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byIndex(space, indexedFields)));
        return cast(this);
    }

    public <Mapped> Flux<Mapped> map(MetaMethod<MetaClass<? extends Storage>, Mapped> function) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byFunction(function)));
        return cast(collect());
    }

    public <Mapped> Flux<Mapped> map(MetaField<? extends MetaClass<Type>, Mapped> field) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byField(field)));
        return cast(collect());
    }

    public Mono<Type> first() {
        return limit(1).offset(1).collect().next();
    }

    public abstract Flux<Type> collect();

    public abstract Mono<Long> count();

    public abstract Mono<Boolean> all(Consumer<Filter<Type>> filter);

    public abstract Mono<Boolean> any(Consumer<Filter<Type>> filter);

    public abstract Mono<Boolean> none(Consumer<Filter<Type>> filter);
}
