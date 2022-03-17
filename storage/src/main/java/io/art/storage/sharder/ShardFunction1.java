package io.art.storage.sharder;

@FunctionalInterface
public interface ShardFunction1<M, P1> {
    int shard(P1 p1);
}
