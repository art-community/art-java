package io.art.http.communicator;

import io.art.core.model.*;
import io.art.http.configuration.*;
import io.art.logging.*;
import io.art.logging.logger.*;
import lombok.*;
import lombok.experimental.*;
import reactor.netty.http.client.*;
import static io.art.http.module.HttpModule.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@UtilityClass
public class HttpCommunicationFactory {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = Logging.logger(HttpCommunication.class);

    public static HttpCommunication createHttpCommunication(HttpConnectorConfiguration connectorConfiguration, CommunicatorActionIdentifier identifier) {
        String connector = connectorConfiguration.getConnector();
        HttpModuleConfiguration moduleConfiguration = httpModule().configuration();
        Supplier<HttpClient> client = () -> createTcpClient(moduleConfiguration.getConnectors().get(connector), identifier);
        return new HttpCommunication(client, moduleConfiguration, connectorConfiguration);
    }

    private static HttpClient createTcpClient(HttpConnectorConfiguration connectorConfiguration, CommunicatorActionIdentifier identifier) {
        HttpClient client = connectorConfiguration.getDecorator().apply(HttpClient.create().baseUrl(connectorConfiguration.getUrl()));
        return client;
    }
}
