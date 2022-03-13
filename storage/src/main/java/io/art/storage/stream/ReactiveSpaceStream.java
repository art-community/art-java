package io.art.storage.stream;

import io.art.core.annotation.*;
import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.filter.implementation.*;
import io.art.storage.filter.model.*;
import io.art.storage.index.*;
import io.art.storage.mapper.*;
import io.art.storage.model.*;
import io.art.storage.sorter.implementation.*;
import io.art.storage.sorter.model.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.model.Tuple.*;
import static io.art.storage.constants.StorageConstants.FilterCondition.*;
import static io.art.storage.constants.StorageConstants.ProcessingOperation.*;
import java.util.*;
import java.util.function.*;

@Public
public abstract class ReactiveSpaceStream<Type> {
    protected List<ProcessingOperator> operators = linkedList();
    protected MetaType<Type> returningType;
    protected Tuple baseKey;

    public ReactiveSpaceStream(MetaType<Type> returningType) {
        this.returningType = returningType;
    }

    public ReactiveSpaceStream(MetaType<Type> returningType, Tuple baseKey) {
        this.returningType = returningType;
        this.baseKey = baseKey;
    }

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
        returningType = cast(space.definition());
        return cast(this);
    }

    public <Mapped, F1> ReactiveSpaceStream<Mapped> map(Index1<Mapped, F1> index, MetaField<? extends MetaClass<Type>, F1> field1) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byIndex(index, tuple(field1))));
        returningType = cast(index.owner().definition());
        return cast(this);
    }

    public <Mapped, F1, F2> ReactiveSpaceStream<Mapped> map(Index2<Mapped, F1, F2> index,
                                                            MetaField<? extends MetaClass<Type>, F1> field1,
                                                            MetaField<? extends MetaClass<Type>, F2> field2) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byIndex(index, tuple(field1, field2))));
        returningType = cast(index.owner().definition());
        return cast(this);
    }

    public <Mapped, F1, F2, F3> ReactiveSpaceStream<Mapped> map(Index3<Mapped, F1, F2, F3> index,
                                                                MetaField<? extends MetaClass<Type>, F1> field1,
                                                                MetaField<? extends MetaClass<Type>, F2> field2,
                                                                MetaField<? extends MetaClass<Type>, F3> field3) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byIndex(index, tuple(field1, field2, field3))));
        returningType = cast(index.owner().definition());
        return cast(this);
    }

    public <Mapped, F1, F2, F3, F4> ReactiveSpaceStream<Mapped> map(Index4<Mapped, F1, F2, F3, F4> index,
                                                                    MetaField<? extends MetaClass<Type>, F1> field1,
                                                                    MetaField<? extends MetaClass<Type>, F2> field2,
                                                                    MetaField<? extends MetaClass<Type>, F3> field3,
                                                                    MetaField<? extends MetaClass<Type>, F4> field4) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byIndex(index, tuple(field1, field2, field3, field4))));
        returningType = cast(index.owner().definition());
        return cast(this);
    }

    public <Mapped, F1, F2, F3, F4, F5> ReactiveSpaceStream<Mapped> map(Index5<Mapped, F1, F2, F3, F4, F5> index,
                                                                        MetaField<? extends MetaClass<Type>, F1> field1,
                                                                        MetaField<? extends MetaClass<Type>, F2> field2,
                                                                        MetaField<? extends MetaClass<Type>, F3> field3,
                                                                        MetaField<? extends MetaClass<Type>, F4> field4,
                                                                        MetaField<? extends MetaClass<Type>, F5> field5) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byIndex(index, tuple(field1, field2, field3, field4, field5))));
        returningType = cast(index.owner().definition());
        return cast(this);
    }

    public <Mapped> Flux<Mapped> map(MetaField<? extends MetaClass<Type>, Mapped> field) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byField(field)));
        returningType = cast(field.type());
        return cast(collect());
    }

    public <Mapped> Flux<Mapped> map(MetaMethod<? extends MetaClass<? extends Storage>, Mapped> function) {
        operators.add(new ProcessingOperator(MAP, new Mapper<Type, Mapped>().byFunction(function)));
        returningType = cast(function.returnType());
        return cast(collect());
    }

    public Mono<Type> first() {
        return limit(1).collect().next();
    }

    public abstract Flux<Type> collect();

    public abstract Mono<Long> count();

    public abstract Mono<Boolean> all(Consumer<Filter<Type>> filter);

    public abstract Mono<Boolean> any(Consumer<Filter<Type>> filter);

    public abstract Mono<Boolean> none(Consumer<Filter<Type>> filter);
}
