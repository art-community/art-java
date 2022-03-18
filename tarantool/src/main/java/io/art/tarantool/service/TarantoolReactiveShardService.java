package io.art.tarantool.service;

import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.serializer.*;
import lombok.*;
import org.msgpack.value.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static org.msgpack.value.ValueFactory.*;

public class TarantoolReactiveShardService<KeyType, ModelType> implements ShardService<ModelType> {
    private final TarantoolModelReader reader;
    private final TarantoolUpdateSerializer updateSerializer;
    private final ImmutableStringValue spaceName;
    private final MetaType<ModelType> spaceMetaType;
    private final TarantoolClientRegistry clients;
    private final TarantoolModelWriter writer;
    private final MetaType<KeyType> keyMeta;
    private final Sharder sharder;

    @Builder
    private TarantoolReactiveShardService(MetaType<KeyType> keyMeta,
                                          MetaClass<ModelType> spaceMeta,
                                          TarantoolClientRegistry clients,
                                          Sharder sharder) {
        this.clients = clients;
        this.spaceMetaType = spaceMeta.definition();
        this.keyMeta = keyMeta;
        this.spaceName = newString(idByDash(spaceMeta.definition().type()));
        writer = tarantoolModule().configuration().getWriter();
        reader = tarantoolModule().configuration().getReader();
        updateSerializer = new TarantoolUpdateSerializer(writer);
        this.sharder = sharder;
    }

}
