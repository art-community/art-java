package io.art.tarantool.module;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.index.*;
import io.art.storage.sharder.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.service.schema.*;
import io.art.tarantool.service.space.*;
import lombok.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.Meta.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

@Public
@RequiredArgsConstructor
public class TarantoolServicesConfigurator {
    private final Map<String, LazyProperty<TarantoolBlockingSpaceService<?, ?>>> spaceServices = map();
    private final Map<String, LazyProperty<TarantoolSchemaService>> schemaServices = map();
    private final Map<String, LazyProperty<Indexes<?>>> indexes = map();
    private final Map<String, LazyProperty<Sharders<?>>> sharders = map();

    public <C> TarantoolServicesConfigurator space(TarantoolSpaceConfigurator<C> configurator) {
        Supplier<MetaField<? extends MetaClass<C>, ?>> idField = configurator.getField();
        Class<? extends Indexes<C>> indexes = configurator.getIndexes();
        Class<C> spaceClass = configurator.getSpaceClass();
        Class<? extends Storage> storageClass = configurator.getStorageClass();
        Class<? extends Sharders<C>> sharders = configurator.getSharders();
        boolean router = configurator.isRouter();

        String storageId = idByDash(storageClass);
        String spaceId = idByDash(spaceClass);
        schemaServices.put(storageId, lazy(() -> router
                ? new TarantoolStorageSchemaService(clients().get(storageId))
                : new TarantoolRouterSchemaService(clients().get(storageId))));

        if (nonNull(sharders)) {
            this.sharders.put(spaceId, lazy(() -> declaration(sharders).creator().singleton()));
            return this;
        }

        if (nonNull(indexes)) {
            spaceServices.put(spaceId, lazy(() -> spaceServiceByField(idFieldByIndexes(indexes), spaceClass, storageId)));
            this.indexes.put(spaceId, lazy(() -> declaration(indexes).creator().singleton()));
            return this;
        }

        spaceServices.put(spaceId, lazy(() -> spaceServiceByField(idField.get().type(), spaceClass, storageId)));
        return this;
    }

    TarantoolServiceRegistry configure() {
        LazyProperty<ImmutableMap<String, TarantoolSchemaService>> schemas = lazy(() -> schemaServices
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get())));
        LazyProperty<ImmutableMap<String, TarantoolBlockingSpaceService<?, ?>>> spaces = lazy(() -> spaceServices
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get())));
        LazyProperty<ImmutableMap<String, Indexes<?>>> indexes = lazy(() -> this.indexes
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get())));
        LazyProperty<ImmutableMap<String, Sharders<?>>> sharders = lazy(() -> this.sharders
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get())));
        return TarantoolServiceRegistry.builder().spaces(spaces).schemas(schemas).indexes(indexes).sharders(sharders).build();
    }

    private static ImmutableMap<String, TarantoolClientRegistry> clients() {
        return tarantoolModule().configuration().getStorageClients();
    }

    private static <C> MetaType<?> idFieldByIndexes(Class<? extends Indexes<C>> indexes) {
        return declaration(indexes).creator()
                .<Indexes<?>>singleton()
                .id()
                .first()
                .type();
    }

    private <C> TarantoolBlockingSpaceService<?, C> spaceServiceByField(MetaType<?> field, Class<C> spaceClass, String storageId) {
        MetaClass<C> spaceDeclaration = declaration(spaceClass);
        TarantoolClientRegistry clients = clients().get(storageId);
        return new TarantoolBlockingSpaceService<>(field, spaceDeclaration, clients);
    }
}
