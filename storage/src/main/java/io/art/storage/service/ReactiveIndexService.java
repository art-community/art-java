package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@SuppressWarnings({VARARGS})
public interface ReactiveIndexService<ModelType> {
    Mono<ModelType> first(Object... keyFields);

    Flux<ModelType> select(Object... keyFields);

    default Flux<ModelType> find(Tuple... keys) {
        return find(asList(keys));
    }

    Flux<ModelType> find(Collection<? extends Tuple> keys);

    Flux<ModelType> find(ImmutableCollection<? extends Tuple> keys);

    default Flux<ModelType> delete(Tuple... keys) {
        return delete(asList(keys));
    }

    Flux<ModelType> delete(Collection<? extends Tuple> keys);

    Flux<ModelType> delete(ImmutableCollection<? extends Tuple> keys);

    Mono<Long> count(Object... keyFields);
}
