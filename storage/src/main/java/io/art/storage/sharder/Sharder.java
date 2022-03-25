package io.art.storage.sharder;

import io.art.core.model.*;

@FunctionalInterface
public interface Sharder<SpaceType> {
    ShardRequest shard(Tuple input);
}
