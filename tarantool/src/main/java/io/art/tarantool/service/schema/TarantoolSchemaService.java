package io.art.tarantool.service.schema;

import io.art.core.collection.*;
import io.art.tarantool.model.*;

public interface TarantoolSchemaService {
    TarantoolSchemaService createSpace(TarantoolSpaceConfiguration configuration);

    TarantoolSchemaService createIndex(TarantoolIndexConfiguration<?, ?> configuration);

    TarantoolSchemaService formatSpace(String space, TarantoolFormatConfiguration configuration);

    TarantoolSchemaService renameSpace(String from, String to);

    TarantoolSchemaService dropSpace(String name);

    TarantoolSchemaService dropIndex(String spaceName, String indexName);

    ImmutableArray<String> spaces();

    ImmutableArray<String> indices(String space);

    default boolean hasSpace(String space) {
        return spaces().stream().anyMatch(space::equals);
    }

    default boolean hasIndex(String space, String index) {
        return indices(space).stream().anyMatch(index::equals);
    }

    default boolean hasIndex(String index) {
        return spaces().stream().flatMap(space -> indices(space).stream()).anyMatch(index::equals);
    }
}
