package io.art.tarantool.service;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.tarantool.registry.*;

public class TarantoolReactiveShardService<KeyType, ModelType> implements ShardService<KeyType, ModelType, TarantoolReactiveShardService<KeyType, ModelType>> {
    public TarantoolReactiveShardService(MetaType<KeyType> keyMeta, MetaClass<ModelType> spaceMeta, TarantoolClientRegistry clients) {
    }

    @Override
    public TarantoolReactiveShardService<KeyType, ModelType> use(Tuple shard) {
        return this;
    }
}
