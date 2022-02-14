package io.art.http.router;

import io.art.core.mime.*;
import io.art.http.configuration.*;
import io.art.http.state.*;
import io.art.meta.model.*;
import io.art.server.method.*;
import io.art.transport.payload.*;
import io.netty.buffer.*;
import lombok.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import reactor.netty.http.websocket.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.mime.MimeType.*;
import static io.art.http.module.HttpModule.*;
import static io.art.http.state.WsLocalState.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.transport.constants.TransportModuleConstants.*;
import static io.art.transport.mime.MimeTypeDataFormatMapper.*;
import static io.art.transport.payload.TransportPayloadReader.*;
import static io.art.transport.payload.TransportPayloadWriter.*;
import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

class WsRouting implements BiFunction<WebsocketInbound, WebsocketOutbound, Publisher<Void>> {
    private final ServiceMethod serviceMethod;
    private final MetaType<?> inputType;
    private final HttpRouteConfiguration routeConfiguration;
    private final MimeType defaultMimeType;
    private final MetaType<?> inputMappingType;
    private final MetaType<?> outputMappingType;
    private final HttpModuleState state;
    private final MetaMethod<?> delegate;

    @Builder(access = PACKAGE)
    private WsRouting(ServiceMethod serviceMethod,
                      HttpRouteConfiguration routeConfiguration,
                      MimeType defaultMimeType,
                      MetaType<?> inputMappingType,
                      MetaType<?> outputMappingType) {
        this.serviceMethod = serviceMethod;
        this.routeConfiguration = routeConfiguration;
        this.defaultMimeType = defaultMimeType;
        this.inputMappingType = inputMappingType;
        this.outputMappingType = outputMappingType;
        state = httpModule().state();
        delegate = serviceMethod.getInvoker().getDelegate();
        inputType = serviceMethod.getInputType();
    }

    @Override
    @SuppressWarnings(CALLING_SUBSCRIBE_IN_NON_BLOCKING_SCOPE)
    public Publisher<Void> apply(WebsocketInbound inbound, WebsocketOutbound outbound) {
        DataFormat inputDataFormat = fromMimeTypes(parseMimeTypes(inbound.headers().get(CONTENT_TYPE), defaultMimeType));
        DataFormat outputDataFormat = fromMimeTypes(parseMimeTypes(inbound.headers().get(ACCEPT), defaultMimeType));
        TransportPayloadReader reader = transportPayloadReader(inputDataFormat);
        TransportPayloadWriter writer = transportPayloadWriter(outputDataFormat);

        WsLocalState localState = wsLocalState(inbound, outbound, routeConfiguration);
        state.wsState(delegate, localState);

        if (isNull(inputType)) {
            Flux<ByteBuf> output = serviceMethod.serve(Flux.empty()).map(value -> writer.write(typed(outputMappingType, value)));
            if (localState.autoClosing()) {
                return localState.outbound().send(output).then();
            }

            localState.outbound().send(output).then().subscribe();
            return localState.closer().asMono();
        }

        Flux<Object> input = localState.inbound()
                .aggregateFrames(routeConfiguration.getWsConfiguration().getAggregateFrames())
                .receive()
                .map(data -> reader.read(inputMappingType, data))
                .filter(data -> !data.isEmpty())
                .map(TransportPayload::getValue);

        Flux<ByteBuf> output = serviceMethod
                .serve(input)
                .map(value -> writer.write(typed(outputMappingType, value)));

        if (localState.autoClosing()) {
            return localState.outbound().send(output).then();
        }

        localState.outbound().send(output).then().subscribe();
        return localState.closer().asMono();
    }
}
