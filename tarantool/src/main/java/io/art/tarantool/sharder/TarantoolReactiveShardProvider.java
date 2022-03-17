package io.art.tarantool.sharder;

import io.art.core.annotation.*;
import io.art.core.model.*;
import io.art.storage.sharder.*;
import io.art.tarantool.service.*;
import lombok.*;

@Public
@RequiredArgsConstructor
public class TarantoolReactiveShardProvider<KeyType, ModelType> implements ShardProvider<KeyType, ModelType, TarantoolReactiveShardService<KeyType, ModelType>> {
    private final TarantoolReactiveShardService<KeyType, ModelType> service;

    @Override
    public TarantoolReactiveShardService<KeyType, ModelType> sharded(Tuple key) {
        return service;
    }
}
