package io.art.storage.sharder;

@FunctionalInterface
public interface ShardFunctionOperator1<M, P1> {
    ShardFunction1<M, P1> shard(SharderFactory factory);
}
