package io.art.http.state;

import io.art.core.annotation.*;
import io.art.http.configuration.*;
import io.netty.handler.codec.http.*;
import lombok.*;
import lombok.experimental.*;
import reactor.netty.http.websocket.*;
import static lombok.AccessLevel.*;

@Getter
@Public
@Accessors(fluent = true)
public class WsLocalState {
    private final WebsocketInbound inbound;
    private final WebsocketOutbound outbound;
    private final HttpRouteConfiguration routeConfiguration;
    private final HttpHeaders requestHeaders;

    private WsLocalState(WebsocketInbound inbound, WebsocketOutbound outbound, HttpRouteConfiguration routeConfiguration) {
        this.inbound = inbound;
        this.outbound = outbound;
        this.routeConfiguration = routeConfiguration;
        requestHeaders = inbound.headers();
    }

    public static WsLocalState wsLocalState(WebsocketInbound inbound, WebsocketOutbound outbound, HttpRouteConfiguration routeConfiguration) {
        return new WsLocalState(inbound, outbound, routeConfiguration);
    }
}
