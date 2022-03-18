package io.art.storage.sharder;

import io.art.core.model.*;

@FunctionalInterface
public interface Sharder<ModelType> {
    ShardRequest shard(Tuple input);
}
