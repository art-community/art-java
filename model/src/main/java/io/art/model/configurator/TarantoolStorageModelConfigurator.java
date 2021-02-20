package io.art.model.configurator;

import io.art.model.implementation.storage.TarantoolSortMethodModel;
import io.art.model.implementation.storage.TarantoolSpaceModel;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;

import static io.art.core.constants.EmptyFunctions.emptyFunction;
import static io.art.core.factory.MapFactory.map;
import static io.art.tarantool.constants.TarantoolModuleConstants.DEFAULT_CLUSTER_NAME;
import static lombok.AccessLevel.PACKAGE;

@Getter(value = PACKAGE)
public class TarantoolStorageModelConfigurator {
    private String cluster = DEFAULT_CLUSTER_NAME;
    private final String space;
    private final Class<?> spaceModelClass;
    private final Class<?> primaryKeyClass;
    private final Map<String, Class<?>> searchers = map();
    private Function<?, Long> bucketIdGenerator = emptyFunction();

    public TarantoolStorageModelConfigurator(String space, Class<?> spaceModelClass, Class<?> primaryKeyClass){
        this.space = space;
        this.spaceModelClass = spaceModelClass;
        this.primaryKeyClass = primaryKeyClass;
    }

    public TarantoolStorageModelConfigurator cluster(String cluster){
        this.cluster = cluster;
        return this;
    }

    public TarantoolStorageModelConfigurator sharded(Function<?, Long> bucketIdGenerator){
        this.bucketIdGenerator = bucketIdGenerator;
        return this;
    }

    public TarantoolStorageModelConfigurator searchBy(String indexName, Class<?> keyClass){
        searchers.put(indexName, keyClass);
        return this;
    }

    TarantoolSpaceModel configure() {
        return TarantoolSpaceModel.builder()
                .cluster(cluster)
                .space(space)
                .spaceModelClass(spaceModelClass)
                .primaryKeyClass(primaryKeyClass)
                .searchers(searchers)
                .bucketIdGenerator(bucketIdGenerator)
                .build();
    }
}
