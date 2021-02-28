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
    private final ImmutableSet.Builder<TarantoolSpaceModelConfigurator> tarantoolConfigurators = immutableSetBuilder();

    public TarantoolStorageModelConfigurator(){
        clusterName = DEFAULT_TARANTOOL_CLUSTER_NAME;
    }

    public final TarantoolStorageModelConfigurator space(String space, Class<?> spaceModelClass, Class<?> primaryKeyClass) {
        tarantoolConfigurators.add(new TarantoolSpaceModelConfigurator(space, spaceModelClass, primaryKeyClass).cluster(clusterName));
        return this;
    }

    @SafeVarargs
    public final TarantoolStorageModelConfigurator space(String space, Class<?> spaceModelClass, Class<?> primaryKeyClass,
                                                    UnaryOperator<TarantoolSpaceModelConfigurator>... configurators) {
        streamOf(configurators)
                .map(configurator -> (Function<TarantoolSpaceModelConfigurator, TarantoolSpaceModelConfigurator>) configurator)
                .reduce(Function::andThen)
                .map(configurator -> configurator.apply(new TarantoolSpaceModelConfigurator(space, spaceModelClass, primaryKeyClass).cluster(clusterName)))
                .ifPresent(tarantoolConfigurators::add);
        return this;
    }

    Stream<TarantoolSpaceModel> configure() {
        return this.tarantoolConfigurators.build()
                .stream()
                .map(TarantoolSpaceModelConfigurator::configure);
    }
}
