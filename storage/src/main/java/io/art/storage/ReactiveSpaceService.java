package io.art.storage;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
public interface ReactiveSpaceService<KeyType, ModelType> {
    Mono<ModelType> findFirst(KeyType key);

    default Flux<ModelType> findAll(KeyType... keys) {
        return findAll(asList(keys));
    }

    Flux<ModelType> findAll(Collection<KeyType> keys);

    Flux<ModelType> findAll(ImmutableCollection<KeyType> keys);

    Mono<ModelType> delete(KeyType key);

    default Flux<ModelType> delete(KeyType... keys) {
        return delete(asList(keys));
    }

    Flux<ModelType> delete(Collection<KeyType> keys);

    Flux<ModelType> delete(ImmutableCollection<KeyType> keys);

    Mono<ModelType> insert(ModelType value);

    default Flux<ModelType> insert(ModelType... value) {
        return insert(Arrays.asList(value));
    }

    Flux<ModelType> insert(Collection<ModelType> value);

    Flux<ModelType> insert(ImmutableCollection<ModelType> value);

    Mono<ModelType> put(ModelType value);

    default Flux<ModelType> put(ModelType... value) {
        return insert(Arrays.asList(value));
    }

    Flux<ModelType> put(Collection<ModelType> value);

    Flux<ModelType> put(ImmutableCollection<ModelType> value);

    Mono<Long> count();

    Mono<Void> truncate();

    <MetaModel extends MetaClass<ModelType>> ReactiveSpaceStream<ModelType, MetaModel> stream(MetaModel model);
}
