package io.art.model.implementation.storage;


import io.art.core.collection.*;
import io.art.storage.space.Space;
import io.art.value.immutable.Value;
import lombok.*;
import static io.art.core.collection.ImmutableMap.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class StorageModuleModel {
    private final ImmutableMap<String, SpaceModel> spaces;

    public Supplier<Space<?,?>> implement(String id,
                                          Function<Optional<io.art.value.immutable.Value>, Optional<?>> toModelMapper,
                                          Function<?, io.art.value.immutable.Value> fromModelMapper,
                                          Function<?, Value> keyMapper) {
        return getStorages().get(id).implement(toModelMapper, fromModelMapper, keyMapper);
    }

    public ImmutableMap<String, SpaceModel> getStorages() {
        return spaces.entrySet().stream().collect(immutableMapCollector(Map.Entry::getKey, Map.Entry::getValue));
    }

}
