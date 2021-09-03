package io.art.rsocket.message;

import io.art.communicator.*;
import io.art.communicator.model.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.configuration.communicator.ws.*;
import io.art.server.method.*;
import lombok.experimental.*;
import static io.art.core.constants.ProtocolConstants.*;
import static io.art.core.constants.StringConstants.*;
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
            message.append(RSOCKET_TCP_SERVER_MESSAGE_PART)
                    .append(isNull(configuration.getTcpServer().getSsl()) ? TCP_SCHEME + SCHEME_DELIMITER : TCPS_SCHEME + SCHEME_DELIMITER)
                    .append(configuration.getTcpServer().getHost())
                    .append(COLON)
                    .append(configuration.getTcpServer().getPort())
                    .append(newLineTabulation(1));
        }
        if (hasWsServer) {
            message.append(RSOCKET_WS_SERVER_MESSAGE_PART)
                    .append(isNull(configuration.getWsServer().getSsl()) ? WS_SCHEME + SCHEME_DELIMITER : WSS_SCHEME + SCHEME_DELIMITER)
                    .append(configuration.getWsServer().getHost())
                    .append(COLON)
                    .append(configuration.getWsServer().getPort())
                    .append(newLineTabulation(1));
        }
        ImmutableMap<ServiceMethodIdentifier, ServiceMethod> methods = configuration.getServer()
                .getMethods()
                .get();
        if (!methods.isEmpty()) {
            message.append(RSOCKET_SERVICE_METHODS_MESSAGE_PART).append(methods
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + SPACE + COLON + SPACE + entry.getValue().getInvoker())
                    .collect(joining(newLineTabulation(2))))
                    .append(newLineTabulation(1));
        }
        if (!tcpConnectors.isEmpty()) {
            message.append(RSOCKET_TCP_CONNECTORS_MESSAGE_PART).append(tcpConnectors.entrySet()
                    .stream()
                    .map(RsocketMessageBuilder::addTcpConnector)
                    .collect(joining(newLineTabulation(2))))
                    .append(newLineTabulation(1));
        }
        if (!wsConnectors.isEmpty()) {
            message.append(RSOCKET_WS_CONNECTORS_MESSAGE_PART).append(wsConnectors.entrySet()
                    .stream()
                    .map(RsocketMessageBuilder::addWsConnector)
                    .collect(joining(newLineTabulation(2))))
                    .append(newLineTabulation(1));

        }
        ImmutableArray<CommunicatorProxy<? extends Communicator>> communicators = configuration.getCommunicator()
                .getConnectors()
                .communicators();
        if (!communicators.isEmpty()) {
            message.append(RSOCKET_COMMUNICATOR_PROXIES_MESSAGE_PART).append(communicators
                    .stream()
                    .map(RsocketMessageBuilder::buildCommunicatorMessage)
                    .collect(joining(newLineTabulation(2))));
        }
        return message.toString();
    }

    private static String buildCommunicatorMessage(CommunicatorProxy<? extends Communicator> communicator) {
        String interfaceName = communicator.getCommunicator().getClass().getInterfaces()[0].getName();
        return interfaceName +
                newLineTabulation(3) +
                communicator
                        .getActions()
                        .entrySet()
                        .stream()
                        .map(action -> format(RSOCKET_CONNECTOR_MESSAGE_PART, action.getValue().getCommunication(), action.getKey(), action.getValue().getMethod()))
                        .collect(joining(newLineTabulation(4)));
    }

    private String addTcpConnector(Map.Entry<String, RsocketTcpConnectorConfiguration> entry) {
        StringBuilder message = new StringBuilder(entry.getKey()).append(SPACE + DASH + SPACE);
        RsocketTcpClientGroupConfiguration groupConfiguration = entry.getValue().getGroupConfiguration();
        if (nonNull(groupConfiguration) && !groupConfiguration.getClientConfigurations().isEmpty()) {
            for (RsocketTcpClientConfiguration clientConfiguration : groupConfiguration.getClientConfigurations()) {
                message.append(isNull(entry.getValue().getCommonConfiguration().getSsl()) ? TCP_SCHEME : TCPS_SCHEME)
                        .append(SCHEME_DELIMITER)
                        .append(clientConfiguration.getHost()).append(COLON).append(clientConfiguration.getPort());
            }
            return message.toString();
        }
        RsocketTcpClientConfiguration clientConfiguration = entry.getValue().getSingleConfiguration();
        return message.append(isNull(entry.getValue().getCommonConfiguration().getSsl()) ? TCP_SCHEME : TCPS_SCHEME)
                .append(SCHEME_DELIMITER)
                .append(clientConfiguration.getHost()).append(COLON).append(clientConfiguration.getPort())
                .toString();
    }

    private String addWsConnector(Map.Entry<String, RsocketWsConnectorConfiguration> entry) {
        StringBuilder message = new StringBuilder(entry.getKey()).append(SPACE + DASH + SPACE);
        RsocketWsClientGroupConfiguration groupConfiguration = entry.getValue().getGroupConfiguration();
        if (nonNull(groupConfiguration) && !groupConfiguration.getClientConfigurations().isEmpty()) {
            for (RsocketWsClientConfiguration clientConfiguration : groupConfiguration.getClientConfigurations()) {
                message.append(isNull(entry.getValue().getCommonConfiguration().getSsl()) ? WS_SCHEME : WSS_SCHEME)
                        .append(SCHEME_DELIMITER)
                        .append(clientConfiguration.getHost()).append(COLON).append(clientConfiguration.getPort());
            }
            return message.toString();
        }
        RsocketWsClientConfiguration clientConfiguration = entry.getValue().getSingleConfiguration();
        return message.append(isNull(entry.getValue().getCommonConfiguration().getSsl()) ? WS_SCHEME : WSS_SCHEME)
                .append(SCHEME_DELIMITER)
                .append(clientConfiguration.getHost()).append(COLON).append(clientConfiguration.getPort())
                .toString();
    }
}
