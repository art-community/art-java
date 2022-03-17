package io.art.storage.sharder;

import io.art.core.annotation.*;
import io.art.core.model.*;

@Public
@FunctionalInterface
public interface ShardFunction {
    Sharder shard(Tuple key);
}
