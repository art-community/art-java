package io.art.tarantool.service;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.tarantool.registry.*;

public class TarantoolReactiveShardService<KeyType, ModelType> implements ShardService<KeyType, ModelType, TarantoolReactiveShardedSpaceService<KeyType, ModelType>> {
    private final TarantoolReactiveShardedSpaceService<KeyType, ModelType> service;

    public TarantoolReactiveShardService(MetaType<KeyType> keyMeta, MetaClass<ModelType> spaceMeta, TarantoolClientRegistry clients) {
        service = new TarantoolReactiveShardedSpaceService<>(keyMeta, spaceMeta, clients);
    }


    @Override
    public TarantoolReactiveShardedSpaceService<KeyType, ModelType> sharded(Tuple key) {
        return service;
    }
}
