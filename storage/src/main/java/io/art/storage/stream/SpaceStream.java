package io.art.storage.stream;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.filter.implementation.*;
import io.art.storage.filter.model.*;
import io.art.storage.mapper.*;
import io.art.storage.sorter.implementation.*;
import io.art.storage.sorter.model.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.constants.StorageConstants.FilterCondition.*;
import static io.art.storage.constants.StorageConstants.ProcessingOperation.*;
import static java.util.Optional.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Public
public abstract class SpaceStream<Type> {
    protected List<ProcessingOperator> operators = linkedList();
    protected MetaType<Type> returningType;

    public SpaceStream(MetaType<Type> returningType) {
        this.returningType = returningType;
    }

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

    public SpaceStream<Type> distinct(MetaField<? extends MetaClass<Type>, ?> field) {
        operators.add(new ProcessingOperator(DISTINCT, field));
        return this;
    }

    public <FieldType> SpaceStream<Type> sort(MetaField<? extends MetaClass<Type>, FieldType> current, UnaryOperator<Sorter<Type, FieldType>> sorter) {
        operators.add(new ProcessingOperator(SORT, sorter.apply(new SorterImplementation<>(current))));
        return this;
    }

    public <FieldType> Optional<Type> max(MetaField<? extends MetaClass<Type>, FieldType> byField) {
        return sort(byField, Sorter::descendant).first();
    }

    public <FieldType> Optional<Type> min(MetaField<? extends MetaClass<Type>, FieldType> byField) {
        return sort(byField, Sorter::ascendant).first();
    }

    public SpaceStream<Type> filter(Consumer<Filter<Type>> filter) {
        FilterImplementation<Type> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        operators.add(new ProcessingOperator(FILTER, newFilter));
        return this;
    }

    public <Mapped> SpaceStream<Mapped> map(MetaClass<Mapped> space, MetaField<? extends MetaClass<Type>, ?> field) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().bySpace(space, field)));
        returningType = cast(space.definition());
        return cast(this);
    }

    public <Mapped> SpaceStream<Mapped> map(MetaClass<Mapped> space, MetaField<? extends MetaClass<Type>, ?>... indexedFields) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byIndex(space, indexedFields)));
        returningType = cast(space.definition());
        return cast(this);
    }

    public <Mapped> Stream<Mapped> map(MetaField<? extends MetaClass<Type>, Mapped> field) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byField(field)));
        return cast(collect().stream());
    }

    public <Mapped> Stream<Mapped> map(MetaMethod<MetaClass<? extends Storage>, Mapped> function) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byFunction(function)));
        return cast(collect().stream());
    }

    public Optional<Type> first() {
        ImmutableArray<Type> array = limit(1).collect();
        return array.isEmpty() ? empty() : of(array.get(0));
    }

    public abstract ImmutableArray<Type> collect();

    public abstract long count();

    public abstract boolean all(Consumer<Filter<Type>> filter);

    public abstract boolean any(Consumer<Filter<Type>> filter);

    public abstract boolean none(Consumer<Filter<Type>> filter);
}
