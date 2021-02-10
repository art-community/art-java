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

    <C, K, V> Supplier<Space<K, V>> implement(Function<C, ? extends Space<K, V>> generatedSpaceBuilder);
}
