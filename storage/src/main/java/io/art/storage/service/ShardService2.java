package io.art.storage.service;

public interface ShardService2<KeyType, ModelType, P1, P2, T extends SpaceService<KeyType, ModelType>> {
    T sharded(P1 p1, P2 p2);
}
