package io.art.tarantool.service;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.tarantool.registry.*;

public class TarantoolShardService<KeyType, ModelType> implements ShardService<KeyType, ModelType, TarantoolSpaceService<KeyType, ModelType>> {
    private final TarantoolSpaceService<KeyType, ModelType> service;

    public TarantoolShardService(MetaType<KeyType> keyMeta, MetaClass<ModelType> spaceMeta, TarantoolClientRegistry clients) {
        service = new TarantoolSpaceService<>(keyMeta, spaceMeta, clients);
    }


    @Override
    public TarantoolSpaceService<KeyType, ModelType> sharded(Tuple key) {
        return service;
    }
}
