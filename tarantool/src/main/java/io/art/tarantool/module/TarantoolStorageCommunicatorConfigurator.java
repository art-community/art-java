package io.art.tarantool.module;

import io.art.communicator.configurator.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.storage.*;
import io.art.tarantool.configuration.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.tarantool.factory.TarantoolCommunicationFactory.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
public class TarantoolStorageCommunicatorConfigurator extends CommunicatorConfigurator<TarantoolStorageCommunicatorConfigurator> {
    private final Map<String, TarantoolStorageConfiguration> storages = map();

    public TarantoolStorageCommunicatorConfigurator storage(Class<? extends Storage> storageClass) {
        return storage(() -> idByDash(storageClass), storageClass, identity());
    }

    public TarantoolStorageCommunicatorConfigurator storage(ConnectorIdentifier connector, Class<? extends Storage> storageClass) {
        return storage(connector, storageClass, identity());
    }

    public TarantoolStorageCommunicatorConfigurator storage(Class<? extends Storage> storageClass, UnaryOperator<TarantoolStorageConnectorConfigurator> configurator) {
        return storage(() -> idByDash(storageClass), storageClass, configurator);
    }

    public TarantoolStorageCommunicatorConfigurator storage(ConnectorIdentifier connector, Class<? extends Storage> storageClass, UnaryOperator<TarantoolStorageConnectorConfigurator> configurator) {
        TarantoolStorageConfiguration configuration = configurator.apply(new TarantoolStorageConnectorConfigurator(connector.id())).configure();
        storages.put(connector.id(), configuration);
        CommunicatorActionFactory factory = (connectorId, actionId) -> createTarantoolCommunication(tarantoolModule().configuration().storage(connectorId));
        register(connector, storageClass, factory);
        return this;
    }

    ImmutableMap<String, TarantoolStorageConfiguration> storages() {
        return immutableMapOf(storages);
    }
}
