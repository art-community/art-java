package ru.art.rsocket.configuration;

import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import ru.art.rsocket.exception.RsocketClientException;
import ru.art.rsocket.model.RsocketCommunicationTargetConfiguration;
import static java.text.MessageFormat.format;
import static ru.art.core.constants.NetworkConstants.LOCALHOST;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.extension.ExceptionExtensions.exceptionIfNull;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.core.network.selector.PortSelector.findAvailableTcpPort;
import static ru.art.rsocket.constants.RsocketModuleConstants.DEFAULT_RSOCKET_PORT;
import static ru.art.rsocket.constants.RsocketModuleConstants.RSOCKET_COMMUNICATION_TARGET_CONFIGURATION_NOT_FOUND;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat.PROTOBUF;
import java.util.Map;

public interface RsocketModuleConfiguration extends ModuleConfiguration {
    String getAcceptorHost();

    int getAcceptorTcpPort();

    int getAcceptorWebSocketPort();

    String getBalancerHost();

    int getBalancerPort();

    RsocketDataFormat getDefaultDataFormat();

    Map<String, RsocketCommunicationTargetConfiguration> getCommunicationTargets();

    default RsocketCommunicationTargetConfiguration getCommunicationTargetConfiguration(String serviceId) {
        return exceptionIfNull(getCommunicationTargets().get(serviceId), new RsocketClientException(format(RSOCKET_COMMUNICATION_TARGET_CONFIGURATION_NOT_FOUND, serviceId))).toBuilder().build();
    }

    @Getter
    class RsocketModuleDefaultConfiguration implements RsocketModuleConfiguration {
        private final RsocketDataFormat defaultDataFormat = PROTOBUF;
        private final String acceptorHost = contextConfiguration().getIpAddress();
        private final int acceptorTcpPort = findAvailableTcpPort();
        private final int acceptorWebSocketPort = findAvailableTcpPort();
        private final String balancerHost = LOCALHOST;
        private final int balancerPort = DEFAULT_RSOCKET_PORT;
        private final Map<String, RsocketCommunicationTargetConfiguration> communicationTargets = mapOf();
    }
}
