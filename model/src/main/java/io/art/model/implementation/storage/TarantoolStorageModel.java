package io.art.model.implementation.storage;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;

import static io.art.core.constants.EmptyFunctions.emptyFunction;
import static io.art.tarantool.constants.TarantoolModuleConstants.DEFAULT_CLUSTER_NAME;

@Getter
@Builder
public class TarantoolStorageModel {
    private final String cluster;
    private final String space;
    private final Class<?> spaceModelClass;
    private final Class<?> primaryKeyClass;
    private final Map<String, TarantoolIndexModel> searchers;
    private final Map<String, TarantoolSortMethodModel> sorters;
    private final Function<?, Long> bucketIdGenerator;

    public String getId(){
        return spaceModelClass.getSimpleName();
    }
}
