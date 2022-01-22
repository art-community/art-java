package io.art.http.message;

import io.art.communicator.*;
import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.http.configuration.*;
import io.art.server.method.*;
import lombok.experimental.*;
import static io.art.core.constants.ProtocolConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.http.constants.HttpModuleConstants.HttpRouteType.*;
import static io.art.http.constants.HttpModuleConstants.Messages.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import java.util.*;

@UtilityClass
public class HttpMessageBuilder {
    public static String httpLaunchedMessage(HttpModuleConfiguration configuration) {
        StringBuilder message = new StringBuilder(HTTP_LAUNCHED_MESSAGE_PART);
        ImmutableMap<String, HttpConnectorConfiguration> connectors = configuration.getConnectors();
        boolean hasServer = configuration.isEnableServer();
        if (hasServer) {
            message.append(format(HTTP_SERVER_MESSAGE_PART,
                    isNull(configuration.getHttpServer().getSsl()) ? HTTP_SCHEME : HTTPS_SCHEME,
                    configuration.getHttpServer().getHost(),
                    EMPTY_STRING + configuration.getHttpServer().getPort()));
        }
        if (!connectors.isEmpty()) {
            String connectorsString = connectors.entrySet()
                    .stream()
                    .map(HttpMessageBuilder::buildConnectorMessage)
                    .collect(joining(newLineTabulation(2)));
            message.append(format(HTTP_CONNECTORS_PART, connectorsString));
        }

        ImmutableArray<HttpRouteConfiguration> roues = configuration.getHttpServer().getRoutes().get();
        if (!roues.isEmpty()) {
            String routesString = roues
                    .stream()
                    .map(route -> buildRouteMessage(configuration, route))
                    .collect(joining(newLineTabulation(2)));
            message.append(format(HTTP_ROUTES_MESSAGE_PART, routesString));
        }
        ImmutableArray<CommunicatorProxy<? extends Communicator>> communicators = configuration.getCommunicator()
                .getPortals()
                .communicators();
        if (!communicators.isEmpty()) {
            String communicatorsString = communicators
                    .stream()
                    .map(communicator -> buildCommunicatorMessage(connectors, communicator))
                    .collect(joining(newLineTabulation(2)));
            message.append(format(HTTP_COMMUNICATORS_MESSAGE_PART, communicatorsString));
        }
        return message.toString();
    }

    private static String buildRouteMessage(HttpModuleConfiguration configuration, HttpRouteConfiguration route) {
        ServiceMethodIdentifier serviceMethodId = route.getServiceMethodId();
        ServiceMethod method = configuration.getServer().getMethods().get().get(serviceMethodId);
        String uri = route.getUri().make(serviceMethodId);
        if (!route.getPathParameters().isEmpty()) {
            if (!uri.endsWith(SLASH)) uri += SLASH;
            uri += route.getPathParameters()
                    .stream()
                    .map(parameter -> COLON + parameter)
                    .collect(joining(SLASH));
        }
        return format(HTTP_ROUTE_METHOD_MESSAGE_PART,
                route.getType().getType().toUpperCase(),
                uri,
                serviceMethodId.getServiceId(),
                serviceMethodId.getMethodId(),
                method.getInvoker().getOwner().definition(),
                method.getInvoker().getDelegate());
    }

    private static String buildCommunicatorMessage(ImmutableMap<String, HttpConnectorConfiguration> connectors, CommunicatorProxy<? extends Communicator> communicator) {
        return communicator
                .getActions()
                .entrySet()
                .stream()
                .map(action -> format(HTTP_COMMUNICATOR_ACTION_MESSAGE_PART,
                        extractRouteType(action.getValue().getMethod().name(), GET),
                        getConnector(connectors, action.getValue()).getUrl() + SLASH + getConnector(connectors, action.getValue()).getUri().make(action.getKey()),
                        action.getValue().getCommunication(),
                        action.getValue().getId().getCommunicatorId(),
                        action.getValue().getId().getActionId(),
                        action.getValue().getOwner().definition(),
                        action.getValue().getMethod()))
                .collect(joining(newLineTabulation(2)));
    }

    private static HttpConnectorConfiguration getConnector(ImmutableMap<String, HttpConnectorConfiguration> connectors, CommunicatorAction action) {
        return connectors.get(action.getCommunication().toString());
    }

    private String buildConnectorMessage(Map.Entry<String, HttpConnectorConfiguration> entry) {
        HttpConnectorConfiguration configuration = entry.getValue();
        return entry.getKey() + SPACE + DASH + SPACE + configuration.getUrl();
    }
}
