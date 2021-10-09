package io.art.http.router;

import io.art.core.mime.*;
import io.art.core.property.*;
import io.art.http.configuration.*;
import io.art.http.state.*;
import io.art.meta.model.*;
import io.art.server.method.*;
import io.art.transport.payload.*;
import io.netty.buffer.*;
import io.netty.handler.codec.http.*;
import lombok.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import reactor.netty.http.server.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.mime.MimeType.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.http.module.HttpModule.*;
import static io.art.http.state.HttpLocalState.*;
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

class HttpRouting implements BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> {
    private final ServiceMethod serviceMethod;
    private final HttpRouteConfiguration routeConfiguration;
    private final MimeType defaultMimeType;
    private final MetaType<?> inputMappingType;
    private final MetaType<?> outputMappingType;
    private final HttpModuleState state;
    private final MetaClass<?> owner;
    private final MetaMethod<?> delegate;
    private final MetaType<?> inputType;

    @Builder(access = PACKAGE)
    private HttpRouting(ServiceMethod serviceMethod,
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
        owner = serviceMethod.getInvoker().getOwner();
        delegate = serviceMethod.getInvoker().getDelegate();
        inputType = serviceMethod.getInputType();
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        HttpHeaders headers = request.requestHeaders();
        DataFormat inputDataFormat = fromMimeType(parseMimeType(headers.get(CONTENT_TYPE), defaultMimeType));
        DataFormat outputDataFormat = fromMimeType(parseMimeType(headers.get(ACCEPT), defaultMimeType));
        TransportPayloadReader reader = transportPayloadReader(inputDataFormat);
        TransportPayloadWriter writer = transportPayloadWriter(outputDataFormat);

        LazyProperty<HttpLocalState> localState = lazy(() -> httpLocalState(request, response, routeConfiguration));
        state.httpState(owner, delegate, localState);

        if (isNull(inputType)) {
            Flux<ByteBuf> output = serviceMethod.serve(Flux.empty()).map(value -> writer.write(typed(outputMappingType, value)));
            return localState.initialized() ? localState.get().response().send(output).then() : response.send(output).then();
        }

        Sinks.One<ByteBuf> emptyCompleter = Sinks.one();

        FLUX<Object> input = (localState.initialized() ? localState.get().request() : request)
                .receive()
                .aggregate()
                .map(data -> reader.read(data, inputMappingType))
                .filter(data -> !data.isEmpty())
                .map(TransportPayload::getValue)
                .flux()
                .doOnComplete(() -> emptyCompleter.emitEmpty(FAIL_FAST));

        Flux<ByteBuf> output = serviceMethod
                .serve(input)
                .map(value -> writer.write(typed(outputMappingType, value)));

        return localState.initialized()
                ? localState.get().response().send(output).then(cast(emptyCompleter.asMono()))
                : response.send(output).then(cast(emptyCompleter.asMono()));
    }
}
