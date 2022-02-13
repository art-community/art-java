package io.art.tarantool.module;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.storage.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.service.*;
import lombok.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.Meta.*;
import static io.art.tarantool.module.TarantoolModule.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolServicesConfigurator {
    private final Map<String, LazyProperty<TarantoolSpaceService<?, ?>>> spaceServices = map();
    private final Map<String, LazyProperty<TarantoolSchemaService>> schemaServices = map();

    public TarantoolServicesConfigurator space(Class<? extends Storage> storageClass, Class<?> keyClass, Class<?> spaceClass) {
        String storageId = idByDash(storageClass);
        String spaceId = idByDash(spaceClass);
        schemaServices.put(storageId, lazy(() -> new TarantoolSchemaService(tarantoolModule().configuration().getStorages().get(storageId))));
        spaceServices.put(spaceId, lazy(() -> new TarantoolSpaceService<>(definition(keyClass), definition(spaceClass), tarantoolModule().configuration().getStorages().get(storageId))));
        return this;
    }

    TarantoolServiceRegistry configure() {
        LazyProperty<ImmutableMap<String, TarantoolSchemaService>> schemas = lazy(() -> schemaServices
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get())));
        LazyProperty<ImmutableMap<String, TarantoolSpaceService<?, ?>>> spaces = lazy(() -> spaceServices
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get())));
        return new TarantoolServiceRegistry(spaces, schemas);
    }
}
