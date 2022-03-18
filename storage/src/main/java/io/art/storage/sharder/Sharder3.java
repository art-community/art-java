package io.art.storage.sharder;

@FunctionalInterface
public interface Sharder3<ModelType, P1, P2, P3> {
    ShardRequest shard(P1 p1, P2 p2, P3 p3);
}
