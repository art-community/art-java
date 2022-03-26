package io.art.tarantool.registry;

import io.art.core.collection.*;
import io.art.storage.index.*;
import io.art.storage.sharder.*;
import io.art.tarantool.connector.*;
import io.art.tarantool.service.schema.*;
import io.art.tarantool.service.space.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;

@Builder
public class TarantoolStorageRegistry {
    private final TarantoolStorageConnector connector;
    private final TarantoolSchemaService schema;
    private final ImmutableMap<String, TarantoolBlockingStorageService<?, ?>> spaces;
    private final ImmutableMap<String, Indexes<?>> indexes;
    private final ImmutableMap<String, Sharders<?>> sharders;
    private final boolean router;

    public TarantoolStorageConnector getConnector() {
        return connector;
    }

    public TarantoolSchemaService getSchema() {
        System.out.println("registry: " + hashCode());
        System.out.println("schema: " + schema.hashCode());
        return schema;
    }

    public <SpaceType, IndexesType extends Indexes<SpaceType>> IndexesType getIndexes(Class<SpaceType> spaceType) {
        return cast(indexes.get(idByDash(spaceType)));
    }

    public <SpaceType, ShardersType extends Sharders<SpaceType>> ShardersType getSharders(Class<SpaceType> spaceType) {
        return cast(sharders.get(idByDash(spaceType)));
    }

    public <KeyType, SpaceType> TarantoolBlockingStorageService<KeyType, SpaceType> getSpace(Class<SpaceType> spaceType) {
        return cast(spaces.get(idByDash(spaceType)));
    }

    public boolean isRouter() {
        return router;
    }
}
