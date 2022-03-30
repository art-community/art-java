package io.art.rsocket.module;

import io.art.communicator.*;
import io.art.communicator.configuration.*;
import io.art.communicator.configurator.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.configuration.communicator.ws.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.rsocket.communicator.RsocketCommunicationFactory.*;
import static io.art.rsocket.module.RsocketModule.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
public class RsocketCommunicatorConfigurator {
    private final Map<String, RsocketTcpConnectorConfiguration> tcpConnectorConfigurations = map();
    private final Map<String, RsocketWsConnectorConfiguration> wsConnectorConfigurations = map();
    private final CommunicatorConfiguratorImplementation delegate = new CommunicatorConfiguratorImplementation();

    public RsocketCommunicatorConfigurator tcp(Class<? extends Communicator> communicatorClass) {
        return tcp(() -> idByDash(communicatorClass), communicatorClass, identity());
    }

    public RsocketCommunicatorConfigurator ws(Class<? extends Communicator> communicatorClass) {
        return ws(() -> idByDash(communicatorClass), communicatorClass, identity());
    }

    public RsocketCommunicatorConfigurator tcp(ConnectorIdentifier connector, Class<? extends Communicator> communicatorClass) {
        return tcp(connector, communicatorClass, identity());
    }

    public RsocketCommunicatorConfigurator ws(ConnectorIdentifier connector, Class<? extends Communicator> communicatorClass) {
        return ws(connector, communicatorClass, identity());
    }

    public RsocketCommunicatorConfigurator tcp(Class<? extends Communicator> communicatorClass, UnaryOperator<RsocketTcpConnectorConfigurator> configurator) {
        return tcp(() -> idByDash(communicatorClass), communicatorClass, configurator);
    }

    public RsocketCommunicatorConfigurator ws(Class<? extends Communicator> communicatorClass, UnaryOperator<RsocketWsConnectorConfigurator> configurator) {
        return ws(() -> idByDash(communicatorClass), communicatorClass, configurator);
    }

    public RsocketCommunicatorConfigurator tcp(ConnectorIdentifier connector,
                                               Class<? extends Communicator> communicatorClass,
                                               UnaryOperator<RsocketTcpConnectorConfigurator> configurator) {
        RsocketTcpConnectorConfigurator connectorConfigurator = configurator.apply(new RsocketTcpConnectorConfigurator(connector.id()));
        RsocketTcpConnectorConfiguration configuration = connectorConfigurator.configure();
        tcpConnectorConfigurations.put(connector.id(), configuration);
        CommunicatorActionFactory communicatorActionFactory = (connectorId, actionId) -> createManagedTcpCommunication(configuration().tcpConnector(connectorId), actionId);
        delegate.register(connector, communicatorClass, communicatorActionFactory);
        return this;
    }

    public RsocketCommunicatorConfigurator ws(ConnectorIdentifier connector,
                                              Class<? extends Communicator> communicatorClass,
                                              UnaryOperator<RsocketWsConnectorConfigurator> configurator) {
        RsocketWsConnectorConfigurator connectorConfigurator = configurator.apply(new RsocketWsConnectorConfigurator(connector.id()));
        RsocketWsConnectorConfiguration configuration = connectorConfigurator.configure();
        wsConnectorConfigurations.put(connector.id(), configuration);
        CommunicatorActionFactory communicatorActionFactory = (connectorId, actionId) -> createManagedWsCommunication(configuration().wsConnector(connectorId), actionId);
        delegate.register(connector, communicatorClass, communicatorActionFactory);
        return this;
    }

    public RsocketCommunicatorConfigurator configure(UnaryOperator<CommunicatorConfigurator> configurator) {
        configurator.apply(delegate);
        return this;
    }

    ImmutableMap<String, RsocketTcpConnectorConfiguration> tcpConnectorConfigurations() {
        return immutableMapOf(tcpConnectorConfigurations);
    }

    ImmutableMap<String, RsocketWsConnectorConfiguration> wsConnectorConfigurations() {
        return immutableMapOf(wsConnectorConfigurations);
    }

    CommunicatorConfiguration createCommunicatorConfiguration(CommunicatorConfiguration current) {
        return delegate.createConfiguration(lazy(() -> configuration().getCommunicator()), current);
    }

    private RsocketModuleConfiguration configuration() {
        return rsocketModule().configuration();
    }
}
