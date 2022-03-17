package io.art.storage.sharder;

import io.art.core.model.*;
import io.art.storage.service.*;

public interface ShardProvider<KeyType, ModelType, ServiceType extends ShardService<KeyType, ModelType, ServiceType>> {
    ServiceType sharded(Tuple key);
}
