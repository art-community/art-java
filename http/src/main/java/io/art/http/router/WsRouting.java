package io.art.http.router;

import io.art.core.mime.*;
import io.art.http.configuration.*;
import io.art.http.state.*;
import io.art.meta.constants.MetaConstants.*;
import io.art.meta.model.*;
import io.art.server.method.*;
import io.art.transport.payload.*;
import lombok.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import reactor.netty.http.websocket.*;
import static io.art.core.mime.MimeType.*;
import static io.art.http.module.HttpModule.*;
import static io.art.http.state.WsLocalState.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.transport.constants.TransportModuleConstants.*;
import static io.art.transport.mime.MimeTypeDataFormatMapper.*;
import static io.art.transport.payload.TransportPayloadReader.*;
import static io.art.transport.payload.TransportPayloadWriter.*;
import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;
import java.util.function.*;

class WsRouting implements BiFunction<WebsocketInbound, WebsocketOutbound, Publisher<Void>> {
    private final ServiceMethod serviceMethod;
    private HttpRouteConfiguration routeConfiguration;
    private final MimeType defaultMimeType;
    private final MetaType<?> inputMappingType;
    private final MetaType<?> outputMappingType;
    private final HttpModuleState state;
    private final MetaClass<?> owner;
    private final MetaMethod<?> delegate;
    private final MetaTypeInternalKind outputKind;

    @Builder(access = PACKAGE)
    private WsRouting(ServiceMethod serviceMethod, HttpRouteConfiguration routeConfiguration, MimeType defaultMimeType, MetaType<?> inputMappingType, MetaType<?> outputMappingType) {
        this.serviceMethod = serviceMethod;
        this.routeConfiguration = routeConfiguration;
        this.defaultMimeType = defaultMimeType;
        this.inputMappingType = inputMappingType;
        this.outputMappingType = outputMappingType;
        state = httpModule().state();
        owner = serviceMethod.getInvoker().getOwner();
        delegate = serviceMethod.getInvoker().getDelegate();
        outputKind = serviceMethod.getOutputType().internalKind();
    }

    @Override
    public Publisher<Void> apply(WebsocketInbound inbound, WebsocketOutbound outbound) {
        DataFormat inputDataFormat = fromMimeType(parseMimeType(inbound.headers().get(CONTENT_TYPE), defaultMimeType));
        DataFormat outputDataFormat = fromMimeType(parseMimeType(inbound.headers().get(ACCEPT), defaultMimeType));
        TransportPayloadReader reader = transportPayloadReader(inputDataFormat);
        TransportPayloadWriter writer = transportPayloadWriter(outputDataFormat);

        WsLocalState localState = wsLocalState(inbound, outbound);
        state.wsState(owner, delegate, localState);

        Flux<Object> input = inbound
                .receive()
                .retain()
                .map(data -> reader.read(data, inputMappingType))
                .filter(data -> !data.isEmpty())
                .map(TransportPayload::getValue);

        if (isNull(outputKind) || outputKind == VOID) {
            Sinks.One<Void> responder = Sinks.one();
            serviceMethod.serve(input.doOnComplete(() -> responder.emitEmpty(FAIL_FAST)));
            return localState.outbound().sendObject(responder.asMono()).then();
        }

        return localState
                .outbound()
                .send(serviceMethod.serve(input).map(value -> writer.write(typed(outputMappingType, value))))
                .then();
    }
}
