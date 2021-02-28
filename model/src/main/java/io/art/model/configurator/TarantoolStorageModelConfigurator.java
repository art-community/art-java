package io.art.model.configurator;

import io.art.core.collection.*;
import io.art.model.implementation.storage.*;
import lombok.*;

import java.util.function.*;
import java.util.stream.*;

import static io.art.core.collection.ImmutableMap.immutableMapCollector;
import static io.art.core.collection.ImmutableSet.immutableSetBuilder;
import static io.art.core.factory.ArrayFactory.streamOf;
import static io.art.tarantool.constants.TarantoolModuleConstants.DEFAULT_TARANTOOL_CLUSTER_NAME;
import static lombok.AccessLevel.PACKAGE;

@Getter(value = PACKAGE)
@RequiredArgsConstructor
public class TarantoolStorageModelConfigurator {
    private final String clusterName;
    private final ImmutableSet.Builder<TarantoolSpaceModelConfigurator<?,?>> tarantoolConfigurators = immutableSetBuilder();

    public TarantoolStorageModelConfigurator(){
        clusterName = DEFAULT_TARANTOOL_CLUSTER_NAME;
    }

    public final <T, K> TarantoolStorageModelConfigurator space(String space, Class<T> spaceModelClass, Class<K> primaryKeyClass) {
        tarantoolConfigurators.add(new TarantoolSpaceModelConfigurator<>(space, spaceModelClass, primaryKeyClass).cluster(clusterName));
        return this;
    }

    @SafeVarargs
    public final <T, K> TarantoolStorageModelConfigurator space(String space, Class<T> spaceModelClass, Class<K> primaryKeyClass,
                                                    UnaryOperator<TarantoolSpaceModelConfigurator<T, K>>... configurators) {
        streamOf(configurators)
                .map(configurator -> (Function<TarantoolSpaceModelConfigurator<T, K>, TarantoolSpaceModelConfigurator<T, K>>) configurator)
                .reduce(Function::andThen)
                .map(configurator -> configurator.apply(new TarantoolSpaceModelConfigurator<>(space, spaceModelClass, primaryKeyClass).cluster(clusterName)))
                .ifPresent(tarantoolConfigurators::add);
        return this;
    }

    Stream<TarantoolSpaceModel> configure() {
        return this.tarantoolConfigurators.build()
                .stream()
                .map(TarantoolSpaceModelConfigurator::configure);
    }
}
