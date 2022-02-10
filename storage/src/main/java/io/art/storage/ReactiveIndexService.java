package io.art.storage;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import reactor.core.publisher.*;
import java.util.*;

@Public
public interface ReactiveIndexService<KeyType, ValueType> {
    Mono<ValueType> findFirst(KeyType key);

    Flux<ValueType> findAll(KeyType... keys);

    Flux<ValueType> findAll(Collection<KeyType> keys);

    Flux<ValueType> findAll(ImmutableCollection<KeyType> keys);

    Mono<ValueType> delete(KeyType key);

    Flux<ValueType> delete(KeyType... keys);

    Flux<ValueType> delete(Collection<KeyType> keys);

    Flux<ValueType> delete(ImmutableCollection<KeyType> keys);

    Mono<Long> count();
}
