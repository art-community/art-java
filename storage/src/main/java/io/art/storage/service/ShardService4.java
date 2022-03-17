package io.art.storage.service;

public interface ShardService4<KeyType, ModelType, P1, P2, P3, P4, T extends SpaceService<KeyType, ModelType>> {
    T shard(P1 p1, P2 p2, P3 p3, P4 p4);
}
