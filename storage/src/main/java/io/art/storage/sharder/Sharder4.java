package io.art.storage.sharder;

@FunctionalInterface
public interface Sharder4<SpaceType, P1, P2, P3, P4> {
    ShardRequest shard(P1 p1, P2 p2, P3 p3, P4 p4);
}
