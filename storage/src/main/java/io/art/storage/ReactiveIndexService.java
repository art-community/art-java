package io.art.storage;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
public interface ReactiveIndexService<KeyType, ValueType> {
    Mono<ValueType> findFirst(KeyType key);

    default Flux<ValueType> findAll(KeyType... keys) {
        return findAll(asList(keys));
    }

    Flux<ValueType> findAll(Collection<KeyType> keys);

    Flux<ValueType> findAll(ImmutableCollection<KeyType> keys);

    Mono<ValueType> delete(KeyType key);

    default Flux<ValueType> delete(KeyType... keys) {
        return findAll(asList(keys));
    }

    Flux<ValueType> delete(Collection<KeyType> keys);

    Flux<ValueType> delete(ImmutableCollection<KeyType> keys);

    Mono<Long> count();
}
