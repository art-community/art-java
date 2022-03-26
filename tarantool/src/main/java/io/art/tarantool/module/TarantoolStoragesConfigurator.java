package io.art.tarantool.module;

import io.art.communicator.configuration.*;
import io.art.communicator.configurator.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.storage.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.registry.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.tarantool.factory.TarantoolCommunicationFactory.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
public class TarantoolStoragesConfigurator {
    private final Map<String, TarantoolStorageConfiguration> configurations = map();
    private final Map<String, LazyProperty<TarantoolStorageRegistry>> registries = map();
    private final CommunicatorConfiguratorImplementation delegate = new CommunicatorConfiguratorImplementation();

    public TarantoolStoragesConfigurator storage(Class<? extends Storage> storageClass) {
        return storage(storageClass, identity());
    }

    public TarantoolStoragesConfigurator storage(Class<? extends Storage> storageClass, UnaryOperator<TarantoolStorageConfigurator> configurator) {
        storage(() -> idByDash(storageClass), storageClass, configurator);
        return this;
    }

    public TarantoolStoragesConfigurator storage(ConnectorIdentifier storageId, Class<? extends Storage> storageClass, UnaryOperator<TarantoolStorageConfigurator> operator) {
        TarantoolStorageConfigurator configurator = operator.apply(new TarantoolStorageConfigurator(storageId.id()));
        configurations.put(storageId.id(), configurator.createConfiguration());
        registries.put(storageId.id(), lazy(configurator::createRegistry));
        CommunicatorActionFactory factory = (communicatorId, actionId) -> createTarantoolCommunication(configuration().storageConfiguration(communicatorId));
        delegate.register(storageId, storageClass, factory);
        return this;
    }

    ImmutableMap<String, TarantoolStorageConfiguration> storageConfigurations() {
        return immutableMapOf(configurations);
    }

    ImmutableMap<String, TarantoolStorageRegistry> createRegistries() {
        return registries.entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get()));
    }

    CommunicatorConfiguration createCommunicatorConfiguration(CommunicatorConfiguration current) {
        return delegate.createConfiguration(lazy(() -> configuration().getCommunicator()), current);
    }

    private TarantoolModuleConfiguration configuration() {
        return tarantoolModule().configuration();
    }
}
