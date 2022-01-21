package io.art.http.module;

import io.art.communicator.*;
import io.art.communicator.configuration.*;
import io.art.communicator.configurator.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.http.configuration.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.http.communicator.HttpCommunicationFactory.*;
import static io.art.http.configuration.HttpConnectorConfiguration.*;
import static io.art.http.module.HttpModule.*;
import static io.art.http.path.HttpCommunicationUri.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
public class HttpCommunicatorConfigurator extends CommunicatorConfigurator<HttpCommunicatorConfigurator> {
    private final Map<String, HttpConnectorConfiguration> connectors = map();

    public HttpCommunicatorConfigurator connector(Class<? extends Portal> connectorClass) {
        return connector(connectorClass, cast(identity()));
    }

    public HttpCommunicatorConfigurator connector(Class<? extends Portal> connectorClass, UnaryOperator<HttpConnectorConfigurationBuilder> configurator) {
        HttpConnectorConfiguration configuration = configurator.apply(httpConnectorConfiguration(idByDash(connectorClass)).toBuilder().uri(byCommunicatorAction())).build();
        connectors.put(idByDash(connectorClass), configuration);
        Function<Class<? extends Communicator>, Communicator> communicatorFunction = communicator -> httpModule()
                .configuration()
                .getCommunicator()
                .getPortals()
                .getCommunicator(connectorClass, communicator)
                .getCommunicator();
        Function<CommunicatorActionIdentifier, Communication> communicationFunction = identifier -> createConfiguredHttpCommunication(configuration);
        registerPortal(connectorClass, communicatorFunction, communicationFunction);
        return this;
    }

    ImmutableMap<String, HttpConnectorConfiguration> configure() {
        return immutableMapOf(connectors);
    }

    CommunicatorConfiguration configureCommunicator(LazyProperty<CommunicatorConfiguration> configurationProvider, CommunicatorConfiguration current) {
        return configure(configurationProvider, current);
    }
}
