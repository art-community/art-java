package io.art.model.implementation.storage;

import io.art.storage.space.Space;
import io.art.tarantool.transaction.TarantoolTransactionManager;

import java.util.function.Function;
import java.util.function.Supplier;

public interface SpaceModel {
    String getId();

    Class<?> getSpaceModelClass();

    Class<?> getPrimaryKeyClass();

    Class<?> getBasicSpaceInterface();

    Supplier<Space<?,?>> implement(Function<?, Space<?,?>> generatedSpaceBuilder);
}
