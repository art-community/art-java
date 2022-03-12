package io.art.tarantool.module;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.index.*;
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
import java.util.function.*;

@Public
@RequiredArgsConstructor
public class TarantoolServicesConfigurator {
    private final Map<String, LazyProperty<TarantoolSpaceService<?, ?>>> spaceServices = map();
    private final Map<String, LazyProperty<TarantoolSchemaService>> schemaServices = map();
    private final Map<String, LazyProperty<Indexes<?>>> indexes = map();

    public <C, M extends MetaClass<C>> TarantoolServicesConfigurator space(Class<? extends Storage> storageClass, Class<C> spaceClass, Supplier<MetaField<M, ?>> idField) {
        String storageId = idByDash(storageClass);
        String spaceId = idByDash(spaceClass);
        schemaServices.put(storageId, lazy(() -> new TarantoolSchemaService(storages().get(storageId))));
        spaceServices.put(spaceId, lazy(() -> new TarantoolSpaceService<>(idField.get().type(), declaration(spaceClass), storages().get(storageId))));
        return this;
    }

    public <C, I extends Indexes<C>> TarantoolServicesConfigurator space(Class<? extends Storage> storageClass, Class<C> spaceClass, Class<I> indexes) {
        String storageId = idByDash(storageClass);
        String spaceId = idByDash(spaceClass);
        schemaServices.put(storageId, lazy(() -> new TarantoolSchemaService(storages().get(storageId))));
        spaceServices.put(spaceId, lazy(() -> new TarantoolSpaceService<>(declaration(indexes).creator().<Indexes<?>>singleton().id().first().type(), declaration(spaceClass), storages().get(storageId))));
        this.indexes.put(spaceId, lazy(() -> declaration(indexes).creator().singleton()));
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
        LazyProperty<ImmutableMap<String, Indexes<?>>> indexes = lazy(() -> this.indexes
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get())));
        return new TarantoolServiceRegistry(spaces, schemas, indexes);
    }

    private static ImmutableMap<String, TarantoolClientRegistry> storages() {
        return tarantoolModule().configuration().getStorageClients();
    }
}
