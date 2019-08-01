package ru.art.rsocket.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport.TCP;
import static ru.art.rsocket.module.RsocketModule.rsocketModule;

@Getter
@Accessors(fluent = true)
@Builder(toBuilder = true, builderMethodName = "rsocketCommunicationTarget")
public class RsocketCommunicationTargetConfiguration {
    @Setter
    private String host;
    @Setter
    private int port;
    @Builder.Default
    private final RsocketTransport transport = TCP;
    @Builder.Default
    private final RsocketDataFormat dataFormat = rsocketModule().getDefaultDataFormat();
}
