package io.art.http.communicator;

import io.art.core.property.*;
import io.art.http.configuration.*;
import io.art.logging.*;
import io.art.logging.logger.*;
import lombok.experimental.*;
import reactor.netty.http.client.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.http.constants.HttpModuleConstants.Messages.*;
import static io.art.http.module.HttpModule.*;
import static java.text.MessageFormat.*;
import java.util.function.*;

@UtilityClass
public class HttpCommunicationFactory {
    private final static LazyProperty<Logger> logger = lazy(() -> Logging.logger(HTTP_COMMUNICATOR_LOGGER));

    public static HttpCommunication createManagedHttpCommunication(HttpConnectorConfiguration connectorConfiguration) {
        String connector = connectorConfiguration.getConnector();
        HttpModuleConfiguration moduleConfiguration = httpModule().configuration();
        Supplier<HttpClient> client = () -> createClient(moduleConfiguration.getConnectors().get(connector));
        return new HttpCommunication(client, moduleConfiguration, connectorConfiguration);
    }

    public static HttpCommunication createDefaultHttpCommunication(HttpConnectorConfiguration connectorConfiguration) {
        return new HttpCommunication(() -> createClient(connectorConfiguration), connectorConfiguration);
    }

    private static HttpClient createClient(HttpConnectorConfiguration connectorConfiguration) {
        HttpClient httpClient = HttpClient.create()
                .compress(connectorConfiguration.isCompress())
                .wiretap(withLogging() && (connectorConfiguration.isWiretapLog() || connectorConfiguration.isVerbose()))
                .followRedirect(connectorConfiguration.isFollowRedirect())
                .disableRetry(!connectorConfiguration.isRetry())
                .keepAlive(connectorConfiguration.isKeepAlive())
                .baseUrl(connectorConfiguration.getUrl());
        apply(connectorConfiguration.getResponseTimeout(), httpClient::responseTimeout);
        if (withLogging() && connectorConfiguration.isVerbose()) {
            httpClient = httpClient
                    .doOnConnected(ignore -> logger.get().info(format(HTTP_COMMUNICATOR_STARTED, connectorConfiguration.getConnector())))
                    .doOnDisconnected(ignore -> logger.get().info(format(HTTP_COMMUNICATOR_STOPPED, connectorConfiguration.getConnector())));
        }
        return connectorConfiguration.getDecorator().apply(httpClient);
    }
}
