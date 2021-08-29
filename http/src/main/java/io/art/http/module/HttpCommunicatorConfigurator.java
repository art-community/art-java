package io.art.http.module;

import io.art.communicator.configuration.*;
import io.art.communicator.configurator.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.communication.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.http.configuration.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.http.communicator.HttpCommunicationFactory.*;
import static io.art.http.configuration.HttpConnectorConfiguration.*;
import static io.art.http.module.HttpModule.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
public class HttpCommunicatorConfigurator extends CommunicatorConfigurator<HttpCommunicatorConfigurator> {
    private final Map<String, HttpConnectorConfiguration> connectors = map();

    public HttpCommunicatorConfigurator connector(Class<? extends Connector> connectorClass) {
        return connector(connectorClass, cast(identity()));
    }

    public HttpCommunicatorConfigurator connector(Class<? extends Connector> connectorClass, UnaryOperator<HttpConnectorConfigurationBuilder> configurator) {
        HttpConnectorConfiguration configuration = configurator.apply(connectorConfiguration(asId(connectorClass)).toBuilder()).build();
        connectors.put(asId(connectorClass), configuration);
        Function<Class<? extends Communicator>, Communicator> communicatorFunction = communicator -> httpModule()
                .configuration()
                .getCommunicator()
                .getConnectors()
                .getCommunicator(connectorClass, communicator)
                .getCommunicator();
        Function<CommunicatorActionIdentifier, Communication> communicationFunction = identifier -> createHttpCommunication(configuration);
        registerConnector(connectorClass, communicatorFunction, communicationFunction);
        return this;
    }

    ImmutableMap<String, HttpConnectorConfiguration> configure() {
        return immutableMapOf(connectors);
    }

    CommunicatorConfiguration configureCommunicator(LazyProperty<CommunicatorConfiguration> configurationProvider, CommunicatorConfiguration current) {
        return configure(configurationProvider, current);
    }
}
