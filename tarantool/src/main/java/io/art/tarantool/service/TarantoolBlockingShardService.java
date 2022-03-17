package io.art.tarantool.service;

import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.tarantool.registry.*;

public class TarantoolBlockingShardService<KeyType, ModelType> implements ShardService<KeyType, ModelType> {
    public TarantoolBlockingShardService(MetaType<KeyType> keyMeta,
                                         MetaClass<ModelType> spaceMeta,
                                         TarantoolClientRegistry clients) {
    }
}
