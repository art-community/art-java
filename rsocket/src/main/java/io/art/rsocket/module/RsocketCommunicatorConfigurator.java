package io.art.rsocket.module;

import io.art.communicator.configuration.*;
import io.art.communicator.configurator.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.communication.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.configuration.communicator.ws.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.rsocket.communicator.RsocketCommunicationFactory.*;
import static io.art.rsocket.module.RsocketModule.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@ForUsing
public class RsocketCommunicatorConfigurator extends CommunicatorConfigurator<RsocketCommunicatorConfigurator> {
    private final Map<String, RsocketTcpConnectorConfiguration> tcpConnectors = map();
    private final Map<String, RsocketWsConnectorConfiguration> wsConnectors = map();

    public RsocketCommunicatorConfigurator tcp(Class<? extends Connector> connectorClass) {
        return tcp(connectorClass, cast(identity()));
    }

    public RsocketCommunicatorConfigurator ws(Class<? extends Connector> connectorClass) {
        return ws(connectorClass, identity());
    }

    public RsocketCommunicatorConfigurator tcp(Class<? extends Connector> connectorClass, UnaryOperator<RsocketTcpConnectorConfigurator> configurator) {
        RsocketTcpConnectorConfigurator connectorConfigurator = configurator.apply(new RsocketTcpConnectorConfigurator(asId(connectorClass)));
        RsocketTcpConnectorConfiguration configuration = connectorConfigurator.configure();
        tcpConnectors.put(asId(connectorClass), configuration);
        Function<Class<? extends Communicator>, Communicator> communicatorFunction = communicator -> rsocketModule()
                .configuration()
                .getCommunicator()
                .getConnectors()
                .getCommunicator(connectorClass, communicator)
                .getCommunicator();
        Function<CommunicatorActionIdentifier, Communication> communicationFunction = identifier -> createTcpCommunication(configuration, identifier);
        registerConnector(connectorClass, communicatorFunction, communicationFunction);
        return this;
    }

    public RsocketCommunicatorConfigurator ws(Class<? extends Connector> connectorClass, UnaryOperator<RsocketWsConnectorConfigurator> configurator) {
        RsocketWsConnectorConfigurator connectorConfigurator = configurator.apply(new RsocketWsConnectorConfigurator(asId(connectorClass)));
        RsocketWsConnectorConfiguration configuration = connectorConfigurator.configure();
        wsConnectors.put(asId(connectorClass), configuration);
        Function<Class<? extends Communicator>, Communicator> communicatorFunction = communicator -> rsocketModule()
                .configuration()
                .getCommunicator()
                .getConnectors()
                .getCommunicator(connectorClass, communicator)
                .getCommunicator();
        Function<CommunicatorActionIdentifier, Communication> communicationFunction = identifier -> createWsCommunication(configuration, identifier);
        registerConnector(connectorClass, communicatorFunction, communicationFunction);
        return this;
    }

    ImmutableMap<String, RsocketTcpConnectorConfiguration> configureTcp() {
        return immutableMapOf(tcpConnectors);
    }

    ImmutableMap<String, RsocketWsConnectorConfiguration> configureWs() {
        return immutableMapOf(wsConnectors);
    }

    CommunicatorConfiguration configureCommunicator(LazyProperty<CommunicatorConfiguration> configurationProvider, CommunicatorConfiguration current) {
        return configure(configurationProvider, current);
    }
}
