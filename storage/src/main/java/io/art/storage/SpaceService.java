package io.art.storage;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
public interface SpaceService<KeyType, ValueType> {
    ValueType findFirst(KeyType key);

    default ImmutableArray<ValueType> findAll(KeyType... keys) {
        return findAll(asList(keys));
    }

    ImmutableArray<ValueType> findAll(Collection<KeyType> keys);

    ImmutableArray<ValueType> findAll(ImmutableCollection<KeyType> keys);

    ValueType delete(KeyType key);


    default ImmutableArray<ValueType> delete(KeyType... keys) {
        return delete(asList(keys));
    }

    ImmutableArray<ValueType> delete(Collection<KeyType> keys);

    ImmutableArray<ValueType> delete(ImmutableCollection<KeyType> keys);


    long count();


    void truncate();


    ValueType insert(ValueType value);

    default ImmutableArray<ValueType> insert(ValueType... value) {
        return insert(asList(value));
    }

    ImmutableArray<ValueType> insert(Collection<ValueType> value);

    ImmutableArray<ValueType> insert(ImmutableCollection<ValueType> value);


    ValueType put(ValueType value);

    default ImmutableArray<ValueType> put(ValueType... value) {
        return put(Arrays.asList(value));
    }

    ImmutableArray<ValueType> put(Collection<ValueType> value);

    ImmutableArray<ValueType> put(ImmutableCollection<ValueType> value);


    ReactiveSpaceService<KeyType, ValueType> reactive();
}
