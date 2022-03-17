package io.art.storage.sharder;

@FunctionalInterface
public interface ShardFunction<M> {
    int shard();

    static <M> ShardFunction<M> constantShard(int constant) {
        return () -> constant;
    }
}
