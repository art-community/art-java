package io.art.tarantool.registry;

import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.storage.*;
import io.art.storage.index.*;
import io.art.storage.sharder.*;
import io.art.tarantool.service.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;

@RequiredArgsConstructor
public class TarantoolServiceRegistry {
    private final LazyProperty<ImmutableMap<String, TarantoolBlockingSpaceService<?, ?>>> spaces;
    private final LazyProperty<ImmutableMap<String, TarantoolSchemaService>> schemas;
    private final LazyProperty<ImmutableMap<String, Indexes<?>>> indexes;
    private final LazyProperty<ImmutableMap<Object, ? extends ShardProvider<?, ?, ?>>> sharders;

    public <KeyType, ValueType> TarantoolBlockingSpaceService<KeyType, ValueType> getSpace(Class<ValueType> type) {
        return cast(spaces.get().get(idByDash(type)));
    }

    public <KeyType, ValueType, P1> ShardProvider<KeyType, ValueType, TarantoolBlockingShardService<KeyType, ValueType>> getSharder(ShardFunction1<ValueType, P1> functor1) {
        return cast(sharders.get().get(functor1));
    }

    public TarantoolSchemaService getSchema(Class<? extends Storage> storageType) {
        return cast(schemas.get().get(idByDash(storageType)));
    }

    public <V, T extends Indexes<V>> T getIndexes(Class<V> spaceType) {
        return cast(indexes.get().get(idByDash(spaceType)));
    }
}
