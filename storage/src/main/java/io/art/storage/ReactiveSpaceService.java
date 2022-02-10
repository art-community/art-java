package io.art.storage;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import reactor.core.publisher.*;
import java.util.*;

@Public
public interface ReactiveSpaceService<KeyType, ValueType> {
    Mono<ValueType> findFirst(KeyType key);

    Flux<ValueType> findAll(KeyType... keys);

    Flux<ValueType> findAll(Collection<KeyType> keys);

    Flux<ValueType> findAll(ImmutableCollection<KeyType> keys);

    Mono<ValueType> delete(KeyType key);

    Flux<ValueType> delete(KeyType... keys);

    Flux<ValueType> delete(Collection<KeyType> keys);

    Flux<ValueType> delete(ImmutableCollection<KeyType> keys);

    Mono<ValueType> insert(ValueType value);

    Flux<ValueType> insert(ValueType... value);

    Flux<ValueType> insert(Collection<ValueType> value);

    Flux<ValueType> insert(ImmutableCollection<ValueType> value);

    Mono<ValueType> put(ValueType value);

    Flux<ValueType> put(ValueType... value);

    Flux<ValueType> put(Collection<ValueType> value);

    Flux<ValueType> put(ImmutableCollection<ValueType> value);

    Mono<ValueType> replace(ValueType value);

    Flux<ValueType> replace(ValueType... value);

    Flux<ValueType> replace(Collection<ValueType> value);

    Mono<Long> count();

    Mono<Void> truncate();
}
