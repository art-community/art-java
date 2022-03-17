package io.art.storage.service;

import io.art.core.model.*;

public interface ShardService<KeyType, ModelType, ServiceType> {
    ServiceType sharded(Tuple key);
}
