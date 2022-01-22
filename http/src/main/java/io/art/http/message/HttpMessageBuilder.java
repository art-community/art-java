package io.art.http.message;

import io.art.communicator.*;
import io.art.communicator.model.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.http.configuration.*;
import io.art.meta.model.*;
import io.art.server.method.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.constants.ProtocolConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
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
                    .map(HttpMessageBuilder::addConnector)
                    .collect(joining(newLineTabulation(2)));
            message.append(format(HTTP_CONNECTORS_MESSAGE_PART, connectorsString));
        }
        ImmutableMap<ServiceMethodIdentifier, ServiceMethod> methods = configuration.getServer().getMethods().get();
        if (!methods.isEmpty()) {
            @AllArgsConstructor
            @EqualsAndHashCode
            class ServiceKey {
                final MetaType<?> type;
                final String id;
            }

            Map<ServiceKey, List<ServiceMethod>> serviceMethods = methods
                    .values()
                    .stream()
                    .collect(groupingBy(entry -> new ServiceKey(entry.getInvoker().getOwner().definition(), entry.getId().getServiceId())));
            String methodsAsString = serviceMethods
                    .entrySet()
                    .stream()
                    .map(entry -> format(HTTP_SERVICE_MESSAGE_PART, entry.getKey().id, entry.getKey().type)
                            + newLineTabulation(3)
                            + entry.getValue()
                            .stream()
                            .map(method -> format(HTTP_SERVICE_METHOD_MESSAGE_PART, method.getId().getMethodId(), method.getInvoker().getDelegate()))
                            .collect(joining(newLineTabulation(3))))
                    .collect(joining(newLineTabulation(2)));
            message.append(format(HTTP_SERVICES_MESSAGE_PART, methodsAsString));
        }
        ImmutableArray<CommunicatorProxy<? extends Communicator>> communicators = configuration.getCommunicator()
                .getPortals()
                .communicators();
        if (!communicators.isEmpty()) {
            String communicatorsString = communicators
                    .stream()
                    .map(HttpMessageBuilder::buildCommunicatorMessage)
                    .collect(joining(newLineTabulation(2)));
            message.append(format(HTTP_COMMUNICATORS_MESSAGE_PART, communicatorsString));
        }
        return message.toString();
    }

    private static String buildCommunicatorMessage(CommunicatorProxy<? extends Communicator> communicator) {
        String interfaceName = format(HTTP_COMMUNICATOR_MESSAGE_PART,
                idByDash(communicator.getCommunicator().getClass().getInterfaces()[0]),
                communicator.getCommunicator().getClass().getInterfaces()[0].getSimpleName());
        String actionsString = communicator
                .getActions()
                .entrySet()
                .stream()
                .map(action -> format(HTTP_COMMUNICATOR_ACTION_MESSAGE_PART,
                        action.getValue().getCommunication(),
                        action.getValue().getId().getActionId(),
                        action.getValue().getMethod()))
                .collect(joining(newLineTabulation(3)));
        return interfaceName + newLineTabulation(3) + actionsString;
    }

    private String addConnector(Map.Entry<String, HttpConnectorConfiguration> entry) {
        HttpConnectorConfiguration configuration = entry.getValue();
        StringBuilder message = new StringBuilder(entry.getKey()).append(SPACE + DASH + SPACE).append(format(HTTP_CONNECTOR_URL_PART, configuration.getUrl()));
        switch (configuration.getUri().strategy()) {
            case AUTOMATIC:
                message.append(format(HTTP_CONNECTOR_URI_PART, HTTP_CONNECTOR_URI_BY_ACTION));
                break;
            case MANUAL:
                message.append(format(HTTP_CONNECTOR_URI_PART, configuration.getUri().manualUri()));
                break;
            case TRANSFORMED:
                message.append(format(HTTP_CONNECTOR_URI_PART, HTTP_CONNECTOR_URI_TRANSFORMED));
                break;
        }
        return message.toString();
    }
}
