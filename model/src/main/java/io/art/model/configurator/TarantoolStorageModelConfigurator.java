package io.art.model.configurator;

import io.art.model.implementation.storage.TarantoolIndexModel;
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
    private final String cluster;
    private final String space;
    private final Class<?> spaceModelClass;
    private final Class<?> primaryKeyClass;
    private final Map<String, TarantoolIndexModel> searchers = map();
    private final Map<String, TarantoolSortMethodModel> sorters = map();
    private Function<?, Long> bucketIdGenerator = emptyFunction();

    public TarantoolStorageModelConfigurator(String space, Class<?> spaceModelClass, Class<?> primaryKeyClass){
        this.cluster = DEFAULT_CLUSTER_NAME;
        this.space = space;
        this.spaceModelClass = spaceModelClass;
        this.primaryKeyClass = primaryKeyClass;
    }

    public TarantoolStorageModelConfigurator(String cluster, String space, Class<?> spaceModelClass, Class<?> primaryKeyClass){
        this.cluster = cluster;
        this.space = space;
        this.spaceModelClass = spaceModelClass;
        this.primaryKeyClass = primaryKeyClass;
    }

    public TarantoolStorageModelConfigurator bucketIdGenerator(Function<?, Long> bucketIdGenerator){
        this.bucketIdGenerator = bucketIdGenerator;
        return this;
    }

    public TarantoolStorageModelConfigurator searchBy(String indexName, Class<?>... fieldTypes){
        searchers.put(indexName, new TarantoolIndexModel(fieldTypes));
        return this;
    }

    public TarantoolStorageModelConfigurator sortBy(String sorterName, TarantoolSortMethodModel sorterModel){
        sorters.put(sorterName, sorterModel);
        return this;
    }

    TarantoolSpaceModel configure() {
        return TarantoolSpaceModel.builder()
                .cluster(cluster)
                .space(space)
                .spaceModelClass(spaceModelClass)
                .primaryKeyClass(primaryKeyClass)
                .searchers(searchers)
                .sorters(sorters)
                .bucketIdGenerator(bucketIdGenerator)
                .build();
    }
}
