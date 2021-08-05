package io.art.rsocket.module;

import io.art.communicator.configuration.*;
import io.art.communicator.configurator.*;
import io.art.communicator.model.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.rsocket.configuration.communicator.http.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.rsocket.communicator.RsocketCommunicationFactory.*;
import static io.art.rsocket.module.RsocketModule.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

public class RsocketCommunicatorConfigurator extends CommunicatorConfigurator {
    private final Map<String, RsocketTcpConnectorConfiguration> tcpConnectors = map();
    private final Map<String, RsocketHttpConnectorConfiguration> httpConnectors = map();

    public RsocketCommunicatorConfigurator tcp(Class<? extends Connector> connectorClass) {
        return tcp(connectorClass, cast(identity()));
    }

    public RsocketCommunicatorConfigurator http(Class<? extends Connector> connectorClass) {
        return http(connectorClass, identity());
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

    public RsocketCommunicatorConfigurator http(Class<? extends Connector> connectorClass, UnaryOperator<RsocketHttpConnectorConfigurator> configurator) {
        RsocketHttpConnectorConfigurator connectorConfigurator = configurator.apply(new RsocketHttpConnectorConfigurator(asId(connectorClass)));
        RsocketHttpConnectorConfiguration configuration = connectorConfigurator.configure();
        httpConnectors.put(asId(connectorClass), configuration);
        Function<Class<? extends Communicator>, Communicator> communicatorFunction = communicator -> rsocketModule()
                .configuration()
                .getCommunicator()
                .getConnectors()
                .getCommunicator(connectorClass, communicator)
                .getCommunicator();
        Function<CommunicatorActionIdentifier, Communication> communicationFunction = identifier -> createHttpCommunication(configuration, identifier);
        registerConnector(connectorClass, communicatorFunction, communicationFunction);
        return this;
    }

    ImmutableMap<String, RsocketTcpConnectorConfiguration> configureTcp() {
        return immutableMapOf(tcpConnectors);
    }

    ImmutableMap<String, RsocketHttpConnectorConfiguration> configureHttp() {
        return immutableMapOf(httpConnectors);
    }

    CommunicatorConfiguration configureCommunicator(LazyProperty<CommunicatorConfiguration> provider, CommunicatorConfiguration current) {
        return configure(provider, current);
    }
}