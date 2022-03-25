package io.art.storage.sharder;

@FunctionalInterface
public interface Sharder1<SpaceType, P1> {
    ShardRequest shard(P1 p1);
}
