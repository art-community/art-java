package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.model.*;

@Public
public interface ShardService<KeyType, ModelType, ServiceType extends ShardService<KeyType, ModelType, ServiceType>> {
    ServiceType use(Tuple shard);
}
