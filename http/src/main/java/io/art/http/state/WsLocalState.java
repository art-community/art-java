package io.art.http.state;

import io.art.core.annotation.*;
import io.art.http.configuration.*;
import lombok.*;
import lombok.experimental.*;
import reactor.netty.http.websocket.*;
import static lombok.AccessLevel.*;

@Getter
@Public
@Accessors(fluent = true)
@AllArgsConstructor(access = PRIVATE)
public class WsLocalState {
    private final WebsocketInbound inbound;
    private final WebsocketOutbound outbound;
    private final HttpRouteConfiguration routeConfiguration;

    public static WsLocalState wsLocalState(WebsocketInbound inbound, WebsocketOutbound outbound, HttpRouteConfiguration routeConfiguration) {
        return new WsLocalState(inbound, outbound, routeConfiguration);
    }
}
