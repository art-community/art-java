package io.art.http.message;

import io.art.communicator.*;
import io.art.communicator.model.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.http.configuration.*;
import io.art.server.method.*;
import lombok.experimental.*;
import static io.art.core.constants.ProtocolConstants.*;
import static io.art.core.constants.StringConstants.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.stream.*;

@UtilityClass
public class HttpMessageBuilder {
    public static String httpLaunchedMessage(HttpModuleConfiguration configuration) {
        StringBuilder message = new StringBuilder("HTTP module launched\n\t");
        if (configuration.isEnableServer()) {
            message.append("HTTP Server - ")
                    .append(HTTP_SCHEME + SCHEME_DELIMITER)
                    .append(configuration.getHttpServer().getHost())
                    .append(COLON)
                    .append(configuration.getHttpServer().getPort())
                    .append("\n\t");
        }
        ImmutableMap<ServiceMethodIdentifier, ServiceMethod> methods = configuration.getServer()
                .getMethods()
                .get();
        if (!methods.isEmpty()) {
            message.append("Routes:\n\t\t").append(configuration.getHttpServer()
                    .getRoutes()
                    .get()
                    .stream()
                    .map(route -> route.getType() + SPACE + route.getPath().route(route.getServiceMethodId()) + " to " + route.getServiceMethodId() + " : " + methods.get(route.getServiceMethodId()).getInvoker())
                    .collect(joining("\n\t\t")));
        }
        ImmutableArray<CommunicatorProxy<? extends Communicator>> communicators = configuration.getCommunicator()
                .getConnectors()
                .communicators();
        if (!communicators.isEmpty()) {
            message.append("Communicator proxies:\n\t\t").append(communicators
                    .stream()
                    .map(communicator -> communicator.getCommunicator().getClass().getInterfaces()[0].getName() + "\n\t\tActions: " + communicator.getActions().entrySet().stream().map(action -> action.getKey().toString()).collect(joining(",")))
                    .collect(joining("\n\t\t")))
                    .append("\n\t");
        }
        ImmutableMap<String, HttpConnectorConfiguration> connectors = configuration.getConnectors();
        if (!connectors.isEmpty()) {
            message.append("\n\t");
            message.append("HTTP Connectors:\n\t\t").append(connectors.entrySet()
                    .stream()
                    .map(HttpMessageBuilder::addConnector)
                    .collect(Collectors.joining("\n\t\t")));
        }
        return message.toString();
    }

    private String addConnector(Map.Entry<String, HttpConnectorConfiguration> entry) {
        StringBuilder message = new StringBuilder(entry.getKey()).append(SPACE + DASH + SPACE);
        String url = entry.getValue().getUrl();
        return message.append(HTTP_SCHEME)
                .append(SCHEME_DELIMITER)
                .append(url)
                .toString();
    }
}
