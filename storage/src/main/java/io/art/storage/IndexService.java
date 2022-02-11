package io.art.storage;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
public interface IndexService<KeyType, ValueType> {
    ValueType findFirst(KeyType key);

    default ImmutableArray<ValueType> findAll(KeyType... keys) {
        return findAll(asList(keys));
    }

    ImmutableArray<ValueType> findAll(Collection<KeyType> keys);

    ImmutableArray<ValueType> findAll(ImmutableCollection<KeyType> keys);

    long count();

    ReactiveIndexService<KeyType, ValueType> reactive();
}
