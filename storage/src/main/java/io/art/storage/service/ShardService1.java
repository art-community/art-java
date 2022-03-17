package io.art.storage.service;

public interface ShardService1<KeyType, ModelType, P1, T extends BlockingSpaceService<KeyType, ModelType>> {
    T sharded(P1 p1);
}
