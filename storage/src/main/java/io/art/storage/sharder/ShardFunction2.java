package io.art.storage.sharder;

@FunctionalInterface
public interface ShardFunction2<M, P1, P2> {
    int shard(P1 p1, P2 p2);
}
