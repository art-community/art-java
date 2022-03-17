package io.art.tarantool.service;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
import io.art.tarantool.registry.*;
import lombok.*;

public class TarantoolReactiveShardService<KeyType, ModelType> implements ShardService<KeyType, ModelType, TarantoolReactiveShardService<KeyType, ModelType>> {
    private final MetaType<KeyType> keyMeta;
    private final MetaClass<ModelType> spaceMeta;
    private final TarantoolClientRegistry clients;
    private final ShardFunction shardFunction;

    @Builder
    public TarantoolReactiveShardService(MetaType<KeyType> keyMeta,
                                         MetaClass<ModelType> spaceMeta,
                                         TarantoolClientRegistry clients,
                                         ShardFunction shardFunction) {
        this.keyMeta = keyMeta;
        this.spaceMeta = spaceMeta;
        this.clients = clients;
        this.shardFunction = shardFunction;
    }

    @Override
    public TarantoolReactiveShardService<KeyType, ModelType> use(Tuple parameters) {
        Sharder sharder = shardFunction.shard(parameters);
        return this;
    }
}
