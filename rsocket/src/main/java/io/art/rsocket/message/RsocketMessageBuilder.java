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
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.stream.*;

@UtilityClass
public class RsocketMessageBuilder {
    public static String rsocketLaunchedMessage(RsocketModuleConfiguration configuration) {
        StringBuilder message = new StringBuilder("RSocket module launched\n\t");
        boolean hasTcpServer = configuration.isEnableTcpServer();
        boolean hasWsServer = configuration.isEnableWsServer();
        ImmutableMap<String, RsocketWsConnectorConfiguration> wsConnectors = configuration.getWsConnectors();
        ImmutableMap<String, RsocketTcpConnectorConfiguration> tcpConnectors = configuration.getTcpConnectors();
        if (hasTcpServer) {
            message.append("TCP Server - ")
                    .append(isNull(configuration.getTcpServer().getSsl()) ? TCP_SCHEME + SCHEME_DELIMITER : TCPS_SCHEME + SCHEME_DELIMITER)
                    .append(configuration.getTcpServer().getHost())
                    .append(COLON)
                    .append(configuration.getTcpServer().getPort())
                    .append("\n\t");
        }
        if (hasWsServer) {
            message.append("WS Server - ")
                    .append(isNull(configuration.getWsServer().getSsl()) ? WS_SCHEME + SCHEME_DELIMITER : WSS_SCHEME + SCHEME_DELIMITER)
                    .append(configuration.getWsServer().getHost())
                    .append(COLON)
                    .append(configuration.getWsServer().getPort())
                    .append("\n\t");
        }
        ImmutableMap<ServiceMethodIdentifier, ServiceMethod> methods = configuration.getServer()
                .getMethods()
                .get();
        if (!methods.isEmpty()) {
            message.append("Service methods:\n\t\t").append(methods
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + SPACE + COLON + SPACE + entry.getValue().getInvoker())
                    .collect(joining("\n\t\t")))
                    .append("\n\t");
        }
        ImmutableArray<CommunicatorProxy<? extends Communicator>> communicators = configuration.getCommunicator()
                .getConnectors()
                .communicators();
        if (!communicators.isEmpty()) {
            message.append("Communicator proxies:\n\t\t").append(communicators
                    .stream()
                    .map(communicator -> communicator.getCommunicator().getClass().getInterfaces()[0].getName() + ":\n\t\t\t" + communicator.getActions().entrySet().stream().map(action -> action.getKey().toString() + " : " + action.getValue().getMethod()).collect(joining("\n\t\t\t\t")))
                    .collect(joining("\n\t\t")))
                    .append("\n\t");
        }
        if (!tcpConnectors.isEmpty()) {
            message.append("TCP Connectors:\n\t\t").append(tcpConnectors.entrySet()
                    .stream()
                    .map(RsocketMessageBuilder::addTcpConnector)
                    .collect(Collectors.joining("\n\t\t")));
        }
        if (!wsConnectors.isEmpty()) {
            message.append("WS Connectors:\n\t\t").append(wsConnectors.entrySet()
                    .stream()
                    .map(RsocketMessageBuilder::addWsConnector)
                    .collect(Collectors.joining("\n\t\t")));

        }
        return message.toString();
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
