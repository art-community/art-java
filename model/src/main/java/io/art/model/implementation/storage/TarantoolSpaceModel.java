package io.art.model.implementation.storage;

import io.art.storage.space.Space;
import io.art.tarantool.space.TarantoolSpace;
import io.art.value.immutable.Value;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
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
    private final Map<String, TarantoolIndexModel> searchers;
    private final Map<String, TarantoolSortMethodModel> sorters;
    private final Function<?, Long> bucketIdGenerator;

    public String getId(){
        return spaceModelClass.getSimpleName();
    }

    public Supplier<Space<?, ?>> implement(Function<Optional<Value>, Optional<?>> toModelMapper,
                                           Function<?, Value> fromModelMapper,
                                           Function<?, Value> keyMapper){
        return ()-> {
            TarantoolSpace<?,?> newSpace = tarantoolInstance(cluster).spaceBuilder()
                .space(space)
                .toModelMapper(cast(toModelMapper))
                .fromModelMapper(cast(fromModelMapper))
                .keyMapper(cast(keyMapper))
                .build();
            newSpace.setBucketIdGenerator(cast(bucketIdGenerator));
            return newSpace;
        };
    }
}
