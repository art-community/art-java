package io.art.tarantool.service;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
import io.art.tarantool.registry.*;
import lombok.*;

public class TarantoolBlockingShardService<KeyType, ModelType> implements ShardService<KeyType, ModelType, TarantoolBlockingShardService<KeyType, ModelType>> {
    @Builder
    public TarantoolBlockingShardService(MetaType<KeyType> keyMeta,
                                         MetaClass<ModelType> spaceMeta,
                                         TarantoolClientRegistry clients,
                                         ShardFunction shardFunction) {
    }

    @Override
    public TarantoolBlockingShardService<KeyType, ModelType> use(Tuple parameters) {
        return this;
    }
}
