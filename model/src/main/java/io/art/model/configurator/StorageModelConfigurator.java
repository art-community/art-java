package io.art.model.configurator;

import io.art.core.collection.ImmutableMap;
import io.art.core.collection.ImmutableSet;
import io.art.model.implementation.storage.SpaceModel;
import io.art.model.implementation.storage.StorageModuleModel;
import io.art.model.implementation.storage.TarantoolSpaceModel;

import static io.art.core.collection.ImmutableMap.immutableMapCollector;
import static io.art.core.collection.ImmutableSet.immutableSetBuilder;
import static java.util.function.Function.identity;

public class StorageModelConfigurator {
    private final ImmutableSet.Builder<TarantoolStorageModelConfigurator> tarantoolConfigurators = immutableSetBuilder();

    public final StorageModelConfigurator tarantool(String space, Class<?> spaceModelClass, Class<?> primaryKeyClass) {
        tarantoolConfigurators.add(new TarantoolStorageModelConfigurator(space, spaceModelClass, primaryKeyClass));
        return this;
    }

    public final StorageModelConfigurator tarantool(String cluster, String space, Class<?> spaceModelClass, Class<?> primaryKeyClass) {
        tarantoolConfigurators.add(new TarantoolStorageModelConfigurator(space, spaceModelClass, primaryKeyClass));
        return this;
    }

    StorageModuleModel configure() {
        ImmutableMap<String, SpaceModel> tarantoolSpaces = this.tarantoolConfigurators.build()
                .stream()
                .map(TarantoolStorageModelConfigurator::configure)
                .collect(immutableMapCollector(TarantoolSpaceModel::getId, identity()));
        return new StorageModuleModel(tarantoolSpaces);
    }
}
