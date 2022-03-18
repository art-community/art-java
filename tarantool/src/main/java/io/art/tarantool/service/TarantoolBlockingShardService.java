package io.art.tarantool.service;

import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
import io.art.tarantool.registry.*;
import lombok.*;
import org.msgpack.value.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static org.msgpack.value.ValueFactory.*;

public class TarantoolBlockingShardService<KeyType, ModelType> implements ShardService<ModelType> {
    private final ImmutableStringValue spaceName;
    private final MetaType<ModelType> spaceMetaType;
    private final TarantoolClientRegistry clients;
    private final TarantoolReactiveShardService<KeyType, ModelType> reactive;

    @Builder
    private TarantoolBlockingShardService(MetaType<KeyType> keyMeta,
                                          MetaClass<ModelType> spaceMeta,
                                          TarantoolClientRegistry clients,
                                          Sharder sharder) {
        this.clients = clients;
        this.spaceMetaType = spaceMeta.definition();
        this.spaceName = newString(idByDash(spaceMeta.definition().type()));
        reactive = TarantoolReactiveShardService.<KeyType, ModelType>builder()
                .keyMeta(keyMeta)
                .spaceMeta(spaceMeta)
                .clients(clients)
                .sharder(sharder)
                .build();
    }
}
