package io.art.storage.service;

import io.art.core.model.*;

public interface ShardService<KeyType, ModelType, ServiceType extends SpaceService<KeyType, ModelType>> {
    ServiceType sharded(Tuple key);
}
