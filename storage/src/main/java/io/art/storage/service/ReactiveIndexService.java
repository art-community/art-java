package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
public interface ReactiveIndexService<KeyType, ModelType> {
    Mono<ModelType> findFirst(KeyType key);

    default Flux<ModelType> findAll(KeyType... keys) {
        return findAll(asList(keys));
    }

    Flux<ModelType> findAll(Collection<KeyType> keys);

    Flux<ModelType> findAll(ImmutableCollection<KeyType> keys);

    Mono<ModelType> delete(KeyType key);

    default Flux<ModelType> delete(KeyType... keys) {
        return findAll(asList(keys));
    }

    Flux<ModelType> delete(Collection<KeyType> keys);

    Flux<ModelType> delete(ImmutableCollection<KeyType> keys);

    Mono<Long> count();
}
