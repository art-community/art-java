package io.art.model.configurator;

import io.art.model.implementation.storage.*;
import lombok.*;

import java.util.*;
import java.util.function.*;

import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static lombok.AccessLevel.*;

@Getter(value = PACKAGE)
public class TarantoolSpaceModelConfigurator {
    private String cluster = DEFAULT_TARANTOOL_CLUSTER_NAME;
    private final String space;
    private final Class<?> spaceModelClass;
    private final Class<?> primaryKeyClass;
    private final Map<String, Class<?>> searchers = map();
    private Function<?, Long> bucketIdGenerator = emptyFunction();

    public TarantoolSpaceModelConfigurator(String space, Class<?> spaceModelClass, Class<?> primaryKeyClass){
        this.space = space;
        this.spaceModelClass = spaceModelClass;
        this.primaryKeyClass = primaryKeyClass;
    }

    protected TarantoolSpaceModelConfigurator cluster(String cluster){
        this.cluster = cluster;
        return this;
    }

    public TarantoolSpaceModelConfigurator sharded(Function<?, Long> bucketIdGenerator){
        this.bucketIdGenerator = bucketIdGenerator;
        return this;
    }

    public TarantoolSpaceModelConfigurator searchBy(String indexName, Class<?> keyClass){
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
