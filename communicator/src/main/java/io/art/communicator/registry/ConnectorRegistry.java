package io.art.communicator.registry;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableArray.*;

@RequiredArgsConstructor
public class ConnectorRegistry {
    private final LazyProperty<ImmutableMap<Class<? extends Connector>, ConnectorContainer>> connectors;

    public <T extends Connector> T getConnector(Class<T> connector) {
        return cast(connectors.get().get(connector).connector);
    }

    public <T extends Communicator> CommunicatorProxy<T> getCommunicator(Class<? extends Connector> connectorClass, Class<? extends Communicator> communicatorClass) {
        return cast(connectors.get().get(connectorClass).communicators.get(communicatorClass));
    }

    public ImmutableArray<? extends Connector> connectors() {
        return connectors.get().values().stream().map(container -> container.connector).collect(immutableArrayCollector());
    }

    public ImmutableArray<CommunicatorProxy<? extends Communicator>> communicators() {
        return connectors.get().values().stream().flatMap(container -> container.communicators.values().stream()).collect(immutableArrayCollector());
    }

    public ImmutableArray<CommunicatorAction> actions() {
        return connectors.get().values()
                .stream()
                .flatMap(container -> container.communicators.values()
                        .stream()
                        .flatMap(communicator -> communicator.getActions().values().stream()))
                .collect(immutableArrayCollector());
    }

    @RequiredArgsConstructor
    public static class ConnectorContainer {
        final ImmutableMap<Class<? extends Communicator>, CommunicatorProxy<? extends Communicator>> communicators;
        final Connector connector;
    }
}
