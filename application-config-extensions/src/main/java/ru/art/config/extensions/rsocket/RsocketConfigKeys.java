package ru.art.config.extensions.rsocket;

public interface RsocketConfigKeys {
    String RSOCKET_SECTION_ID = "rsocket";
    String RSOCKET_BALANCER_SECTION_ID = "rsocket.balancer";
    String RSOCKET_ACCEPTOR_SECTION_ID = "rsocket.acceptor";
    String RSOCKET_ACCEPTOR_TCP_PORT = "tcpPort";
    String RSOCKET_ACCEPTOR_WEB_SOCKET_PORT = "webSocketPort";
    String DEFAULT_DATA_FORMAT = "defaultDataFormat";
    String TRANSPORT = "transport";
}
