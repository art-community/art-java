package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@SuppressWarnings({VARARGS})
public interface ReactiveIndexService<ModelType> {
    default Mono<ModelType> findFirst(Object... keys) {
        return findFirst(asList(keys));
    }

    Mono<ModelType> findFirst(Collection<Object> keys);

    Mono<ModelType> findFirst(ImmutableCollection<Object> keys);

    default Flux<ModelType> findAll(Object... keys) {
        return findAll(asList(keys));
    }

    Flux<ModelType> findAll(Collection<Object> keys);

    Flux<ModelType> findAll(ImmutableCollection<Object> keys);

    default Flux<ModelType> delete(Object... keys) {
        return delete(asList(keys));
    }

    Flux<ModelType> delete(Collection<Object> keys);

    Flux<ModelType> delete(ImmutableCollection<Object> keys);

    Mono<Long> count();
}
