package io.art.storage.updater;

import io.art.meta.model.*;
import io.art.storage.constants.StorageConstants.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.constants.StorageConstants.UpdateOperation.*;
import java.util.*;

@Getter
public class UpdaterImplementation<T> implements Updater<T> {
    private final List<UpdateOperator> operators = linkedList();

    @Override
    public Updater<T> add(MetaField<? extends MetaClass<T>, ? extends Number> field, Number value) {
        operators.add(new UpdateOperator(ADD, field, linkedListOf(value)));
        return this;
    }

    @Override
    public Updater<T> subtract(MetaField<? extends MetaClass<T>, ? extends Number> field, Number value) {
        operators.add(new UpdateOperator(SUBTRACT, field, linkedListOf(value)));
        return this;
    }

    @Override
    public Updater<T> bitwiseAnd(MetaField<? extends MetaClass<T>, ? extends Number> field, Number value) {
        operators.add(new UpdateOperator(BITWISE_AND, field, linkedListOf(value)));
        return this;
    }

    @Override
    public Updater<T> bitwiseOr(MetaField<? extends MetaClass<T>, ? extends Number> field, Number value) {
        operators.add(new UpdateOperator(BITWISE_OR, field, linkedListOf(value)));
        return this;
    }

    @Override
    public Updater<T> bitwiseXor(MetaField<? extends MetaClass<T>, ? extends Number> field, Number value) {
        operators.add(new UpdateOperator(BITWISE_XOR, field, linkedListOf(value)));
        return this;
    }

    @Override
    public Updater<T> splice(MetaField<? extends MetaClass<T>, String> field, int startIndex, int endIndex, String delimiter) {
        operators.add(new UpdateOperator(SPLICE, field, linkedListOf(startIndex, endIndex, delimiter)));
        return this;
    }

    @Override
    public <F> Updater<T> set(MetaField<? extends MetaClass<T>, F> field, F value) {
        operators.add(new UpdateOperator(SET, field, linkedListOf(value)));
        return this;
    }

    @Getter
    @AllArgsConstructor
    public static class UpdateOperator {
        private final UpdateOperation operation;
        private final MetaField<?, ?> field;
        private final List<?> values;
    }
}
