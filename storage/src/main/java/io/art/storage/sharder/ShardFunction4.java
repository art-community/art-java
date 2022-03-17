package io.art.storage.sharder;

@FunctionalInterface
public interface ShardFunction4<M, P1, P2, P3, P4> {
    int shard(P1 p1, P2 p2, P3 p3, P4 p4);
}
