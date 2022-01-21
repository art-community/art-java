package io.art.rsocket.module;

import io.art.communicator.*;
import io.art.communicator.configuration.*;
import io.art.communicator.configurator.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
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

@Public
public class RsocketCommunicatorConfigurator extends CommunicatorConfigurator<RsocketCommunicatorConfigurator> {
    private final Map<String, RsocketTcpConnectorConfiguration> tcpConnectors = map();
    private final Map<String, RsocketWsConnectorConfiguration> wsConnectors = map();

    public RsocketCommunicatorConfigurator tcp(Class<? extends Portal> portalClass) {
        return tcp(portalClass, cast(identity()));
    }

    public RsocketCommunicatorConfigurator ws(Class<? extends Portal> portalClass) {
        return ws(portalClass, identity());
    }

    public RsocketCommunicatorConfigurator tcp(Class<? extends Portal> portalClass, UnaryOperator<RsocketTcpConnectorConfigurator> configurator) {
        RsocketTcpConnectorConfigurator portalConfigurator = configurator.apply(new RsocketTcpConnectorConfigurator(idByDash(portalClass)));
        RsocketTcpConnectorConfiguration configuration = portalConfigurator.configure();
        tcpConnectors.put(idByDash(portalClass), configuration);
        Function<Class<? extends Communicator>, Communicator> communicatorFunction = communicator -> rsocketModule()
                .configuration()
                .getCommunicator()
                .getPortals()
                .getCommunicator(portalClass, communicator)
                .getCommunicator();
        Function<CommunicatorActionIdentifier, Communication> communicationFunction = identifier -> createTcpCommunication(configuration, identifier);
        registerPortal(portalClass, communicatorFunction, communicationFunction);
        return this;
    }

    public RsocketCommunicatorConfigurator ws(Class<? extends Portal> portalClass, UnaryOperator<RsocketWsConnectorConfigurator> configurator) {
        RsocketWsConnectorConfigurator portalConfigurator = configurator.apply(new RsocketWsConnectorConfigurator(idByDash(portalClass)));
        RsocketWsConnectorConfiguration configuration = portalConfigurator.configure();
        wsConnectors.put(idByDash(portalClass), configuration);
        Function<Class<? extends Communicator>, Communicator> communicatorFunction = communicator -> rsocketModule()
                .configuration()
                .getCommunicator()
                .getPortals()
                .getCommunicator(portalClass, communicator)
                .getCommunicator();
        Function<CommunicatorActionIdentifier, Communication> communicationFunction = identifier -> createWsCommunication(configuration, identifier);
        registerPortal(portalClass, communicatorFunction, communicationFunction);
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
