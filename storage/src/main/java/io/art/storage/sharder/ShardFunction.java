package io.art.storage.sharder;

import static io.art.storage.constants.StorageConstants.*;
import static io.art.storage.sharder.SharderFactory.*;

@FunctionalInterface
public interface ShardFunction<M> {
    Sharder shard();

    static <T, M> ShardFunction<M> constantShard(T constant) {
        return () -> constantSharder(constant);
    }

    static <T, M> ShardFunction<M> constantShard(T constant, ShardAlgorithm algorithm) {
        return () -> constantSharder(constant, algorithm);
    }
}
