package ru.adk.config.extensions.rsocket;

import lombok.Getter;
import ru.adk.rsocket.configuration.RsocketModuleConfiguration.RsocketModuleDefaultConfiguration;
import ru.adk.rsocket.model.RsocketCommunicationTargetConfiguration;
import static ru.adk.config.extensions.ConfigExtensions.*;
import static ru.adk.config.extensions.common.CommonConfigKeys.*;
import static ru.adk.config.extensions.rsocket.RsocketConfigKeys.*;
import static ru.adk.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.adk.core.context.Context.context;
import static ru.adk.core.extension.ExceptionExtensions.ifException;
import static ru.adk.core.extension.NullCheckingExtensions.getOrElse;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RSOCKET_MODULE_ID;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import static ru.adk.rsocket.model.RsocketCommunicationTargetConfiguration.rsocketCommunicationTarget;
import static ru.adk.rsocket.module.RsocketModule.rsocketModuleState;
import java.util.Map;

@Getter
public class RsocketAgileConfiguration extends RsocketModuleDefaultConfiguration {
    private RsocketDataFormat dataFormat;
    private String acceptorHost = super.getAcceptorHost();
    private int acceptorTcpPort = super.getAcceptorTcpPort();
    private int acceptorWebSocketPort = super.getAcceptorWebSocketPort();
    private String balancerHost;
    private int balancerPort;
    private Map<String, RsocketCommunicationTargetConfiguration> communicationTargets;

    public RsocketAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        dataFormat = ifException(() -> RsocketDataFormat.valueOf(configString(RSOCKET_SECTION_ID, DEFAULT_DATA_FORMAT).toUpperCase()), super.getDefaultDataFormat());
        String newAcceptorHost = configString(RSOCKET_ACCEPTOR_SECTION_ID, HOST, super.getAcceptorHost());
        boolean restart = !acceptorHost.equals(newAcceptorHost);
        int newAcceptorTcpPort = configInt(RSOCKET_ACCEPTOR_SECTION_ID, RSOCKET_ACCEPTOR_TCP_PORT, super.getAcceptorTcpPort());
        restart |= acceptorTcpPort != newAcceptorTcpPort;
        int newAcceptorWebSocketPort = configInt(RSOCKET_ACCEPTOR_SECTION_ID, RSOCKET_ACCEPTOR_WEB_SOCKET_PORT, super.getAcceptorWebSocketPort());
        restart |= acceptorWebSocketPort != newAcceptorWebSocketPort;
        balancerHost = configString(RSOCKET_BALANCER_SECTION_ID, HOST, super.getBalancerHost());
        balancerPort = configInt(RSOCKET_BALANCER_SECTION_ID, PORT, super.getBalancerPort());
        communicationTargets = configMap(RSOCKET_SECTION_ID, TARGETS, config -> rsocketCommunicationTarget()
                .host(ifEmpty(config.getString(HOST), balancerHost))
                .port(getOrElse(config.getInt(PORT), balancerPort))
                .dataFormat(ifException(() -> RsocketDataFormat.valueOf(config.getString(DEFAULT_DATA_FORMAT).toUpperCase()), super.getDefaultDataFormat()))
                .build(), super.getCommunicationTargets());
        if (restart && context().hasModule(RSOCKET_MODULE_ID)) {
            rsocketModuleState().getServer().restart();
        }
    }
}