package io.art.storage.sharder;

@FunctionalInterface
public interface ShardFunction1<M, P1> {
    Sharder shard(P1 key);
}
