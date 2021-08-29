package io.art.http.communicator;

import io.art.http.configuration.*;
import io.art.logging.*;
import io.art.logging.logger.*;
import lombok.*;
import lombok.experimental.*;
import reactor.netty.http.client.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.http.constants.HttpModuleConstants.Messages.*;
import static io.art.http.module.HttpModule.*;
import static java.text.MessageFormat.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@UtilityClass
public class HttpCommunicationFactory {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = Logging.logger(HttpCommunication.class);

    public static HttpCommunication createHttpCommunication(HttpConnectorConfiguration connectorConfiguration) {
        String connector = connectorConfiguration.getConnector();
        HttpModuleConfiguration moduleConfiguration = httpModule().configuration();
        Supplier<HttpClient> client = () -> createTcpClient(moduleConfiguration.getConnectors().get(connector));
        return new HttpCommunication(client, moduleConfiguration, connectorConfiguration);
    }

    private static HttpClient createTcpClient(HttpConnectorConfiguration connectorConfiguration) {
        HttpClient httpClient = HttpClient.create()
                .compress(connectorConfiguration.isCompress())
                .wiretap(withLogging() && (connectorConfiguration.isWiretapLog() || connectorConfiguration.isVerbose()))
                .followRedirect(connectorConfiguration.isFollowRedirect())
                .disableRetry(!connectorConfiguration.isRetry())
                .keepAlive(connectorConfiguration.isKeepAlive())
                .baseUrl(connectorConfiguration.getUrl());
        if (withLogging()) {
            httpClient = httpClient
                    .doOnConnected(ignore -> getLogger().info(format(HTTP_COMMUNICATOR_STARTED, connectorConfiguration.getConnector())))
                    .doOnDisconnected(ignore -> getLogger().info(format(HTTP_COMMUNICATOR_STOPPED, connectorConfiguration.getConnector())));
        }
        return connectorConfiguration.getDecorator().apply(httpClient);
    }
}
