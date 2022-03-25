package io.art.http.module;

import io.art.communicator.*;
import io.art.communicator.configuration.*;
import io.art.communicator.configurator.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.http.configuration.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.http.communicator.HttpCommunicationFactory.*;
import static io.art.http.configuration.HttpConnectorConfiguration.*;
import static io.art.http.module.HttpModule.*;
import static io.art.http.path.HttpCommunicationUri.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
public class HttpCommunicatorConfigurator {
    private final CommunicatorConfiguratorImplementation delegate = new CommunicatorConfiguratorImplementation();
    private final Map<String, HttpConnectorConfiguration> connectors = map();

    public HttpCommunicatorConfigurator connector(Class<? extends Communicator> communicatorClass) {
        return connector(() -> idByDash(communicatorClass), communicatorClass, identity());
    }

    public HttpCommunicatorConfigurator connector(ConnectorIdentifier connector, Class<? extends Communicator> communicatorClass) {
        return connector(connector, communicatorClass, identity());
    }

    public HttpCommunicatorConfigurator connector(Class<? extends Communicator> communicatorClass, UnaryOperator<HttpConnectorConfigurationBuilder> configurator) {
        return connector(() -> idByDash(communicatorClass), communicatorClass, configurator);
    }

    public HttpCommunicatorConfigurator connector(ConnectorIdentifier connector, Class<? extends Communicator> communicatorClass, UnaryOperator<HttpConnectorConfigurationBuilder> configurator) {
        HttpConnectorConfiguration configuration = configurator.apply(httpConnectorConfiguration(connector.id()).toBuilder().uri(byCommunicatorAction())).build();
        connectors.put(connector.id(), configuration);
        CommunicatorActionFactory factory = (connectorId, actionId) -> createManagedHttpCommunication(configuration().connector(connectorId));
        delegate.register(connector, communicatorClass, factory);
        return this;
    }


    public HttpCommunicatorConfigurator configure(UnaryOperator<CommunicatorConfigurator> configurator) {
        configurator.apply(delegate);
        return this;
    }

    ImmutableMap<String, HttpConnectorConfiguration> connectors() {
        return immutableMapOf(connectors);
    }

    CommunicatorConfiguration createCommunicatorConfiguration(CommunicatorConfiguration current) {
        return delegate.createConfiguration(lazy(() -> configuration().getCommunicator()), current);
    }

    private HttpModuleConfiguration configuration() {
        return httpModule().configuration();
    }
}
