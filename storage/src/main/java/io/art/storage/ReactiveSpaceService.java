package io.art.storage;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
public interface ReactiveSpaceService<KeyType, ValueType> {
    Mono<ValueType> findFirst(KeyType key);

    default Flux<ValueType> findAll(KeyType... keys) {
        return findAll(asList(keys));
    }

    Flux<ValueType> findAll(Collection<KeyType> keys);

    Flux<ValueType> findAll(ImmutableCollection<KeyType> keys);

    Mono<ValueType> delete(KeyType key);

    default Flux<ValueType> delete(KeyType... keys) {
        return delete(asList(keys));
    }

    Flux<ValueType> delete(Collection<KeyType> keys);

    Flux<ValueType> delete(ImmutableCollection<KeyType> keys);

    Mono<ValueType> insert(ValueType value);

    default Flux<ValueType> insert(ValueType... value) {
        return insert(Arrays.asList(value));
    }

    Flux<ValueType> insert(Collection<ValueType> value);

    Flux<ValueType> insert(ImmutableCollection<ValueType> value);

    Mono<ValueType> put(ValueType value);

    default Flux<ValueType> put(ValueType... value) {
        return insert(Arrays.asList(value));
    }

    Flux<ValueType> put(Collection<ValueType> value);

    Flux<ValueType> put(ImmutableCollection<ValueType> value);

    Mono<Long> count();

    Mono<Void> truncate();
}
