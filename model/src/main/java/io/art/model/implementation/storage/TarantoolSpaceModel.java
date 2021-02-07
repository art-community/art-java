package io.art.model.implementation.storage;

import io.art.storage.space.Space;
import io.art.tarantool.space.TarantoolSpace;
import io.art.tarantool.transaction.TarantoolTransactionManager;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.art.core.caster.Caster.cast;
import static io.art.tarantool.module.TarantoolModule.tarantoolInstance;

@Getter
@Builder
public class TarantoolSpaceModel implements SpaceModel {
    private final String cluster;
    private final String space;
    private final Class<?> spaceModelClass;
    private final Class<?> primaryKeyClass;
    private final Map<String, Class<?>> searchers;
    private final Map<String, TarantoolSortMethodModel> sorters;
    private final Function<?, Long> bucketIdGenerator;

    public String getId(){
        return spaceModelClass.getSimpleName();
    }

    @Override
    public Class<?> getBasicSpaceInterface() {
        return TarantoolSpace.class;
    }

    @Override
    public Supplier<Space<?, ?>> implement(Function<?, Space<?, ?>> generatedSpaceBuilder) {
        return () -> generatedSpaceBuilder.apply(cast(tarantoolInstance(cluster).getTransactionManager()));
    }
}
