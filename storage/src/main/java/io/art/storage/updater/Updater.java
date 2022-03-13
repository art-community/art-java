package io.art.storage.updater;

import io.art.meta.model.*;

public interface Updater<T> {
    Updater<T> add(MetaField<? extends MetaClass<T>, ? extends Number> field, Number value);

    Updater<T> subtract(MetaField<? extends MetaClass<T>, ? extends Number> field, Number value);

    default Updater<T> increment(MetaField<? extends MetaClass<T>, ? extends Number> field, Number value) {
        return add(field, 1);
    }

    default Updater<T> decrement(MetaField<? extends MetaClass<T>, ? extends Number> field, Number value) {
        return subtract(field, 1);
    }

    Updater<T> bitwiseAnd(MetaField<? extends MetaClass<T>, ? extends Number> field, Number value);

    Updater<T> bitwiseOr(MetaField<? extends MetaClass<T>, ? extends Number> field, Number value);

    Updater<T> bitwiseXor(MetaField<? extends MetaClass<T>, ? extends Number> field, Number value);

    Updater<T> splice(MetaField<? extends MetaClass<T>, String> field, int startIndex, int endIndex, String delimiter);

    <F> Updater<T> insert(MetaField<? extends MetaClass<T>, F> field, F value);

    <F> Updater<T> set(MetaField<? extends MetaClass<T>, F> field, F value);

    default Updater<T> delete(MetaField<? extends MetaClass<T>, ?> field) {
        return delete(field, 1);
    }

    Updater<T> delete(MetaField<? extends MetaClass<T>, ?> startField, int count);
}
