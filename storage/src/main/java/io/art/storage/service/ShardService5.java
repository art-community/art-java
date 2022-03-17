package io.art.storage.service;

public interface ShardService5<KeyType, ModelType, P1, P2, P3, P4, P5, T extends SpaceService<KeyType, ModelType>> {
    T shard(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5);
}
