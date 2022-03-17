package io.art.tarantool.service;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.tarantool.registry.*;

public class TarantoolShardService<KeyType, ModelType> implements ShardService<KeyType, ModelType, TarantoolShardedSpaceService<KeyType, ModelType>> {
    private final TarantoolShardedSpaceService<KeyType, ModelType> service;

    public TarantoolShardService(MetaType<KeyType> keyMeta, MetaClass<ModelType> spaceMeta, TarantoolClientRegistry clients) {
        service = new TarantoolShardedSpaceService<>(keyMeta, spaceMeta, clients);
    }


    @Override
    public TarantoolShardedSpaceService<KeyType, ModelType> sharded(Tuple key) {
        return service;
    }
}
