package io.art.tarantool.module;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import io.art.storage.index.*;
import io.art.storage.sharder.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.connector.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.service.schema.*;
import io.art.tarantool.service.space.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.Meta.*;
import static java.util.Objects.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
public class TarantoolStorageConfigurator {
    private boolean router;
    private final String storageId;
    private UnaryOperator<TarantoolStorageConnectorConfigurator> configurator = identity();
    private final Map<String, LazyProperty<TarantoolBlockingStorageService<?, ?>>> spaces = map();
    private final Map<String, LazyProperty<Indexes<?>>> indexes = map();
    private final Map<String, LazyProperty<Sharders<?>>> sharders = map();
    private final TarantoolStorageConnector connector;

    TarantoolStorageConfigurator(String storageId) {
        this.storageId = storageId;
        connector = new TarantoolStorageConnector(storageId);
    }


    public TarantoolStorageConfigurator router() {
        router = true;
        return this;
    }

    public TarantoolStorageConfigurator connector(UnaryOperator<TarantoolStorageConnectorConfigurator> configurator) {
        this.configurator = configurator;
        return this;
    }

    public <SpaceType> TarantoolStorageConfigurator space(Class<SpaceType> spaceClass, UnaryOperator<TarantoolSpaceConfigurator<SpaceType>> operator) {
        TarantoolSpaceConfigurator<SpaceType> configurator = operator.apply(new TarantoolSpaceConfigurator<>(spaceClass));
        Supplier<MetaField<? extends MetaClass<SpaceType>, ?>> idField = configurator.getField();
        Class<? extends Indexes<SpaceType>> indexes = configurator.getIndexes();
        Class<? extends Sharders<SpaceType>> sharders = configurator.getSharders();
        String spaceId = idByDash(spaceClass);

        if (nonNull(sharders)) {
            router();
            this.sharders.putIfAbsent(spaceId, lazy(() -> declaration(sharders).creator().singleton()));
        }

        if (nonNull(indexes)) {
            spaces.putIfAbsent(spaceId, lazy(() -> createSpaceService(findIdFieldType(indexes), spaceClass)));
            this.indexes.putIfAbsent(spaceId, lazy(() -> declaration(indexes).creator().singleton()));
            return this;
        }

        spaces.putIfAbsent(spaceId, lazy(() -> createSpaceService(idField.get().type(), spaceClass)));
        return this;
    }

    TarantoolStorageRegistry createRegistry() {

        TarantoolSchemaService schema = router ? new TarantoolRouterSchemaService(connector) : new TarantoolStorageSchemaService(connector);

        ImmutableMap<String, TarantoolBlockingStorageService<?, ?>> spaces = this.spaces
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get()));

        ImmutableMap<String, Indexes<?>> indexes = this.indexes
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get()));

        ImmutableMap<String, Sharders<?>> sharders = this.sharders
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get()));

        return TarantoolStorageRegistry.builder()
                .connector(connector)
                .schema(schema)
                .spaces(spaces)
                .indexes(indexes)
                .sharders(sharders)
                .build();
    }

    TarantoolStorageConfiguration createConfiguration() {
        return configurator.apply(new TarantoolStorageConnectorConfigurator(storageId)).configure();
    }

    private static <C> MetaType<?> findIdFieldType(Class<? extends Indexes<C>> indexes) {
        return declaration(indexes).creator()
                .<Indexes<?>>singleton()
                .id()
                .first()
                .type();
    }

    private <C> TarantoolBlockingStorageService<?, C> createSpaceService(MetaType<?> field, Class<C> spaceClass) {
        MetaClass<C> spaceDeclaration = declaration(spaceClass);
        return new TarantoolBlockingStorageService<>(field, spaceDeclaration, connector);
    }
}
