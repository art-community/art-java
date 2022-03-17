package io.art.tarantool.service;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.tarantool.registry.*;

public class TarantoolBlockingShardService<KeyType, ModelType> implements ShardService<KeyType, ModelType, TarantoolBlockingShardService<KeyType, ModelType>> {
    public TarantoolBlockingShardService(MetaType<KeyType> keyMeta,
                                         MetaClass<ModelType> spaceMeta,
                                         TarantoolClientRegistry clients) {
    }

    @Override
    public TarantoolBlockingShardService<KeyType, ModelType> use(Tuple shard) {
        return this;
    }
}
