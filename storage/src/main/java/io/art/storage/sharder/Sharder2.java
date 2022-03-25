package io.art.storage.sharder;

@FunctionalInterface
public interface Sharder2<SpaceType, P1, P2> {
    ShardRequest shard(P1 p1, P2 p2);
}
