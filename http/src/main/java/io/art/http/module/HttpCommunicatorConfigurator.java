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

    public HttpCommunicatorConfigurator portal(Class<? extends Portal> portalClass) {
        return portal(portalClass, cast(identity()));
    }

    public HttpCommunicatorConfigurator portal(Class<? extends Portal> portalClass, UnaryOperator<HttpConnectorConfigurationBuilder> configurator) {
        HttpConnectorConfiguration configuration = configurator.apply(httpConnectorConfiguration(idByDash(portalClass)).toBuilder().uri(byCommunicatorAction())).build();
        connectors.put(idByDash(portalClass), configuration);
        Function<Class<? extends Communicator>, Communicator> communicatorFunction = communicator -> httpModule()
                .configuration()
                .getCommunicator()
                .getPortals()
                .getCommunicator(portalClass, communicator)
                .getCommunicator();
        Function<CommunicatorActionIdentifier, Communication> communicationFunction = identifier -> createConfiguredHttpCommunication(configuration);
        registerPortal(portalClass, communicatorFunction, communicationFunction);
        return this;
    }

    ImmutableMap<String, HttpConnectorConfiguration> connectors() {
        return immutableMapOf(connectors);
    }

    CommunicatorConfiguration configureCommunicator(LazyProperty<CommunicatorConfiguration> configurationProvider, CommunicatorConfiguration current) {
        return configure(configurationProvider, current);
    }
}
