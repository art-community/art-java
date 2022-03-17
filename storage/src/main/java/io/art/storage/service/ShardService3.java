package io.art.storage.service;

public interface ShardService3<KeyType, ModelType, P1, P2, P3, T extends SpaceService<KeyType, ModelType>> {
    T sharded(P1 p1, P2 p2, P3 p3);
}
