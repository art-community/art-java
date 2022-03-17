package io.art.storage.sharder;

@FunctionalInterface
public interface ShardFunctionOperator2<M, P1, P2> {
    ShardFunction shard(SharderFactory factory);
}
