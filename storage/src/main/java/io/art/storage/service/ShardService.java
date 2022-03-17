package io.art.storage.service;

import io.art.core.model.*;

public interface ShardService<KeyType, ModelType, T extends SpaceService<KeyType, ModelType>> {
    T shard(Tuple key);
}
