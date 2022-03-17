package io.art.tarantool.sharder;

import io.art.core.annotation.*;
import io.art.core.model.*;
import io.art.storage.sharder.*;
import io.art.tarantool.service.*;
import lombok.*;

@Public
@RequiredArgsConstructor
public class TarantoolBlockingShardProvider<KeyType, ModelType> implements ShardProvider<KeyType, ModelType, TarantoolBlockingShardService<KeyType, ModelType>> {
    private final TarantoolBlockingShardService<KeyType, ModelType> service;

    @Override
    public TarantoolBlockingShardService<KeyType, ModelType> sharded(Tuple key) {
        return service;
    }
}
