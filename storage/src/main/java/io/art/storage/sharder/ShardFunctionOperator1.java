package io.art.storage.sharder;

@FunctionalInterface
public interface ShardFunctionOperator1<M, P1> {
    ShardFunction shard(SharderFactory factory, P1 p1);
}
