package io.art.model.implementation.storage;

import io.art.storage.space.Space;
import io.art.value.immutable.Value;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface SpaceModel {
    String getId();

    Supplier<Space<?,?>> implement(Function<Optional<Value>, Optional<?>> toModelMapper,
                                   Function<?, io.art.value.immutable.Value> fromModelMapper,
                                   Function<?, Value> keyMapper);
}
