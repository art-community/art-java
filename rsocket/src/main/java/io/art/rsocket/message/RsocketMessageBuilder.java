package io.art.rsocket.message;

import io.art.communicator.*;
import io.art.communicator.model.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.configuration.communicator.ws.*;
import io.art.server.method.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.constants.ProtocolConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import java.util.*;

@UtilityClass
public class RsocketMessageBuilder {
    public static String rsocketLaunchedMessage(RsocketModuleConfiguration configuration) {
        StringBuilder message = new StringBuilder(RSOCKET_LAUNCHED_MESSAGE_PART);
        boolean hasTcpServer = configuration.isEnableTcpServer();
        boolean hasWsServer = configuration.isEnableWsServer();
        ImmutableMap<String, RsocketWsConnectorConfiguration> wsConnectors = configuration.getWsConnectors();
        ImmutableMap<String, RsocketTcpConnectorConfiguration> tcpConnectors = configuration.getTcpConnectors();
        if (hasTcpServer) {
            message.append(format(RSOCKET_TCP_SERVER_MESSAGE_PART,
                    isNull(configuration.getTcpServer().getSsl()) ? TCP_SCHEME : TCPS_SCHEME,
                    configuration.getTcpServer().getHost(),
                    EMPTY_STRING + configuration.getTcpServer().getPort()));
        }
        if (hasWsServer) {
            message.append(format(RSOCKET_WS_SERVER_MESSAGE_PART,
                    isNull(configuration.getWsServer().getSsl()) ? WS_SCHEME : WSS_SCHEME,
                    configuration.getWsServer().getHost(),
                    EMPTY_STRING + configuration.getWsServer().getPort()));
        }
        if (!tcpConnectors.isEmpty()) {
            String connectorsString = tcpConnectors.entrySet()
                    .stream()
                    .map(RsocketMessageBuilder::addTcpConnector)
                    .collect(joining(newLineTabulation(2)));
            message.append(format(RSOCKET_TCP_CONNECTORS_MESSAGE_PART, connectorsString));
        }
        if (!wsConnectors.isEmpty()) {
            String connectorsString = wsConnectors.entrySet()
                    .stream()
                    .map(RsocketMessageBuilder::addWsConnector)
                    .collect(joining(newLineTabulation(2)));
            message.append(format(RSOCKET_WS_CONNECTORS_MESSAGE_PART, connectorsString));
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
                    .map(entry -> format(RSOCKET_SERVICE_MESSAGE_PART, entry.getKey().id, entry.getKey().type)
                            + newLineTabulation(3)
                            + entry.getValue()
                            .stream()
                            .map(method -> format(RSOCKET_SERVICE_METHOD_MESSAGE_PART, method.getId().getMethodId(), method.getInvoker().getDelegate()))
                            .collect(joining(newLineTabulation(3))))
                    .collect(joining(newLineTabulation(2)));
            message.append(format(RSOCKET_SERVICES_MESSAGE_PART, methodsAsString));
        }
        ImmutableArray<CommunicatorProxy<? extends Communicator>> communicators = configuration.getCommunicator()
                .getConnectors()
                .communicators();
        if (!communicators.isEmpty()) {
            String communicatorsString = communicators
                    .stream()
                    .map(RsocketMessageBuilder::buildCommunicatorMessage)
                    .collect(joining(newLineTabulation(2)));
            message.append(format(RSOCKET_COMMUNICATORS_MESSAGE_PART, communicatorsString));
        }
        return message.toString();
    }

    private static String buildCommunicatorMessage(CommunicatorProxy<? extends Communicator> communicator) {
        String interfaceName = format(RSOCKET_COMMUNICATOR_MESSAGE_PART,
                asId(communicator.getCommunicator().getClass().getInterfaces()[0]),
                communicator.getCommunicator().getClass().getInterfaces()[0].getSimpleName());
        String actionsString = communicator
                .getActions()
                .entrySet()
                .stream()
                .map(action -> format(RSOCKET_COMMUNICATOR_ACTION_MESSAGE_PART,
                        action.getValue().getCommunication(),
                        action.getValue().getId().getActionId(),
                        action.getValue().getMethod()))
                .collect(joining(newLineTabulation(3)));
        return interfaceName + newLineTabulation(3) + actionsString;
    }

    private String addTcpConnector(Map.Entry<String, RsocketTcpConnectorConfiguration> entry) {
        StringBuilder message = new StringBuilder(entry.getKey()).append(SPACE + DASH + SPACE);
        RsocketTcpClientGroupConfiguration groupConfiguration = entry.getValue().getGroupConfiguration();
        if (nonNull(groupConfiguration)) {
            ImmutableSet<RsocketTcpClientConfiguration> clientConfigurations = groupConfiguration.getClientConfigurations();
            if (!clientConfigurations.isEmpty()) {
                for (RsocketTcpClientConfiguration clientConfiguration : clientConfigurations) {
                    message.append(format(URI_FORMAT,
                            isNull(entry.getValue().getCommonConfiguration().getSsl()) ? TCP_SCHEME : TCPS_SCHEME,
                            clientConfiguration.getHost(),
                            EMPTY_STRING + clientConfiguration.getPort()));
                }
                return message.toString();
            }
        }
        RsocketTcpClientConfiguration clientConfiguration = entry.getValue().getSingleConfiguration();
        return message
                .append(format(URI_FORMAT,
                        isNull(entry.getValue().getCommonConfiguration().getSsl()) ? TCP_SCHEME : TCPS_SCHEME,
                        clientConfiguration.getHost(),
                        EMPTY_STRING + clientConfiguration.getPort()))
                .toString();
    }

    private String addWsConnector(Map.Entry<String, RsocketWsConnectorConfiguration> entry) {
        StringBuilder message = new StringBuilder(entry.getKey()).append(SPACE + DASH + SPACE);
        RsocketWsClientGroupConfiguration groupConfiguration = entry.getValue().getGroupConfiguration();
        if (nonNull(groupConfiguration)) {
            ImmutableSet<RsocketWsClientConfiguration> clientConfigurations = groupConfiguration.getClientConfigurations();
            if (!clientConfigurations.isEmpty()) {
                for (RsocketWsClientConfiguration clientConfiguration : clientConfigurations) {
                    message.append(format(URI_FORMAT,
                            isNull(entry.getValue().getCommonConfiguration().getSsl()) ? WS_SCHEME : WSS_SCHEME,
                            clientConfiguration.getHost(),
                            EMPTY_STRING + clientConfiguration.getPort()));
                }
                return message.toString();
            }
        }
        RsocketWsClientConfiguration clientConfiguration = entry.getValue().getSingleConfiguration();
        return message
                .append(format(URI_FORMAT,
                        isNull(entry.getValue().getCommonConfiguration().getSsl()) ? WS_SCHEME : WSS_SCHEME,
                        clientConfiguration.getHost(),
                        EMPTY_STRING + clientConfiguration.getPort()))
                .toString();
    }
}
