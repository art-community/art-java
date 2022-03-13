package io.art.storage.stream;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.filter.implementation.*;
import io.art.storage.filter.model.*;
import io.art.storage.index.*;
import io.art.storage.mapper.*;
import io.art.storage.model.*;
import io.art.storage.sorter.implementation.*;
import io.art.storage.sorter.model.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.model.Tuple.*;
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

    public <Mapped, F1> SpaceStream<Mapped> map(Index1<Mapped, F1> index, MetaField<? extends MetaClass<Type>, F1> field1) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byIndex(index, tuple(field1))));
        returningType = cast(index.owner().definition());
        return cast(this);
    }

    public <Mapped, F1, F2> SpaceStream<Mapped> map(Index2<Mapped, F1, F2> index,
                                                    MetaField<? extends MetaClass<Type>, F1> field1,
                                                    MetaField<? extends MetaClass<Type>, F2> field2) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byIndex(index, tuple(field1, field2))));
        returningType = cast(index.owner().definition());
        return cast(this);
    }

    public <Mapped, F1, F2, F3> SpaceStream<Mapped> map(Index3<Mapped, F1, F2, F3> index,
                                                        MetaField<? extends MetaClass<Type>, F1> field1,
                                                        MetaField<? extends MetaClass<Type>, F2> field2,
                                                        MetaField<? extends MetaClass<Type>, F3> field3) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byIndex(index, tuple(field1, field2, field3))));
        returningType = cast(index.owner().definition());
        return cast(this);
    }

    public <Mapped, F1, F2, F3, F4> SpaceStream<Mapped> map(Index4<Mapped, F1, F2, F3, F4> index,
                                                            MetaField<? extends MetaClass<Type>, F1> field1,
                                                            MetaField<? extends MetaClass<Type>, F2> field2,
                                                            MetaField<? extends MetaClass<Type>, F3> field3,
                                                            MetaField<? extends MetaClass<Type>, F4> field4) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byIndex(index, tuple(field1, field2, field3, field4))));
        returningType = cast(index.owner().definition());
        return cast(this);
    }

    public <Mapped, F1, F2, F3, F4, F5> SpaceStream<Mapped> map(Index5<Mapped, F1, F2, F3, F4, F5> index,
                                                                MetaField<? extends MetaClass<Type>, F1> field1,
                                                                MetaField<? extends MetaClass<Type>, F2> field2,
                                                                MetaField<? extends MetaClass<Type>, F3> field3,
                                                                MetaField<? extends MetaClass<Type>, F4> field4,
                                                                MetaField<? extends MetaClass<Type>, F5> field5) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byIndex(index, tuple(field1, field2, field3, field4, field5))));
        returningType = cast(index.owner().definition());
        return cast(this);
    }

    public <Mapped> Stream<Mapped> map(MetaField<? extends MetaClass<Type>, Mapped> field) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byField(field)));
        returningType = cast(field.type());
        return cast(collect().stream());
    }

    public <Mapped> Stream<Mapped> map(MetaMethod<? extends MetaClass<? extends Storage>, Mapped> function) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byFunction(function)));
        returningType = cast(function.returnType());
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
