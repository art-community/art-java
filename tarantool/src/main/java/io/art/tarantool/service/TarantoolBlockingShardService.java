package io.art.tarantool.service;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.tarantool.registry.*;

public class TarantoolBlockingShardService<KeyType, ModelType> implements ShardService<KeyType, ModelType, TarantoolBlockingShardedSpaceService<KeyType, ModelType>> {
    private final TarantoolBlockingShardedSpaceService<KeyType, ModelType> service;

    public TarantoolBlockingShardService(MetaType<KeyType> keyMeta, MetaClass<ModelType> spaceMeta, TarantoolClientRegistry clients) {
        service = new TarantoolBlockingShardedSpaceService<>(keyMeta, spaceMeta, clients);
    }


    @Override
    public TarantoolBlockingShardedSpaceService<KeyType, ModelType> sharded(Tuple key) {
        return service;
    }
}
