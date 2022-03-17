package io.art.storage.sharder;

@FunctionalInterface
public interface ShardFunction5<M, P1, P2, P3, P4, P5> {
    Sharder shard(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5);
}
