package io.art.http.router;

import io.art.core.mime.*;
import io.art.core.property.*;
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
import static io.art.core.mime.MimeType.*;
import static io.art.core.property.LazyProperty.*;
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
import java.util.concurrent.atomic.*;
import java.util.function.*;

class WsRouting implements BiFunction<WebsocketInbound, WebsocketOutbound, Publisher<Void>> {
    private final ServiceMethod serviceMethod;
    private final MetaType<?> inputType;
    private final MetaType<?> outputType;
    private final HttpRouteConfiguration routeConfiguration;
    private final MimeType defaultMimeType;
    private final MetaType<?> inputMappingType;
    private final MetaType<?> outputMappingType;
    private final HttpModuleState state;
    private final MetaClass<?> owner;
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
        owner = serviceMethod.getInvoker().getOwner();
        delegate = serviceMethod.getInvoker().getDelegate();
        inputType = serviceMethod.getInputType();
        outputType = serviceMethod.getOutputType();
    }

    @Override
    public Publisher<Void> apply(WebsocketInbound inbound, WebsocketOutbound outbound) {
        DataFormat inputDataFormat = fromMimeType(parseMimeType(inbound.headers().get(CONTENT_TYPE), defaultMimeType));
        DataFormat outputDataFormat = fromMimeType(parseMimeType(inbound.headers().get(ACCEPT), defaultMimeType));
        TransportPayloadReader reader = transportPayloadReader(inputDataFormat);
        TransportPayloadWriter writer = transportPayloadWriter(outputDataFormat);

        LazyProperty<WsLocalState> localState = lazy(() -> wsLocalState(inbound, outbound, routeConfiguration));
        state.wsState(owner, delegate, localState);

        if (isNull(inputType)) {
            Flux<ByteBuf> output = serviceMethod.serve(Flux.empty()).map(value -> writer.write(typed(outputMappingType, value)));
            return localState.initialized() ? localState.get().outbound().send(output).then() : outbound.send(output).then();
        }

        if (inputType.internalKind() != FLUX) {
            Flux<Object> input = (localState.initialized() ? localState.get().inbound() : inbound)
                    .aggregateFrames()
                    .receive()
                    .map(data -> reader.read(data, inputMappingType))
                    .filter(data -> !data.isEmpty())
                    .map(TransportPayload::getValue);

            Flux<ByteBuf> output = serviceMethod
                    .serve(input)
                    .map(value -> writer.write(typed(outputMappingType, value)));

            return localState.initialized()
                    ? localState.get().outbound().send(output).then()
                    : outbound.send(output).then();
        }

        FluxInputHandler fluxInputHandler = new FluxInputHandler();

        fluxInputHandler.subscribeOnInput(inbound, reader, localState);
        fluxInputHandler.subscribeOnOutput(writer);

        return localState.initialized()
                ? localState.get().outbound().send(fluxInputHandler.outputElements.asFlux()).then()
                : outbound.send(fluxInputHandler.outputElements.asFlux()).then();
    }

    private class FluxInputHandler {
        private final AtomicBoolean inputProduced = new AtomicBoolean(false);
        private final AtomicBoolean inputComplete = new AtomicBoolean(false);
        private final AtomicBoolean outputComplete = new AtomicBoolean(false);
        private final AtomicBoolean done = new AtomicBoolean(false);
        private final Sinks.Many<Object> inputElements = Sinks.many().unicast().onBackpressureBuffer();
        private final Sinks.Many<ByteBuf> outputElements = Sinks.many().unicast().onBackpressureBuffer();

        private void subscribeOnInput(WebsocketInbound inbound, TransportPayloadReader reader, LazyProperty<WsLocalState> localState) {
            (localState.initialized() ? localState.get().inbound() : inbound)
                    .aggregateFrames()
                    .receive()
                    .map(data -> reader.read(data, inputMappingType))
                    .filter(data -> !data.isEmpty())
                    .map(TransportPayload::getValue)
                    .doOnNext(this::handleNextInput)
                    .doOnError(error -> inputElements.emitError(error, FAIL_FAST))
                    .doOnComplete(this::handleInputComplete)
                    .subscribe();
        }

        private void subscribeOnOutput(TransportPayloadWriter writer) {
            serviceMethod
                    .serve(inputElements.asFlux())
                    .map(value -> writer.write(typed(outputMappingType, value)))
                    .doOnNext(element -> outputElements.emitNext(element, FAIL_FAST))
                    .doOnError(error -> outputElements.emitError(error, FAIL_FAST))
                    .doOnComplete(this::handleOutputComplete)
                    .subscribe();
        }

        private void handleOutputComplete() {
            if (done.get()) return;
            outputComplete.compareAndSet(false, true);
            if (inputProduced.get() || inputComplete.get()) {
                if (done.compareAndSet(false, true)) {
                    outputElements.emitComplete(FAIL_FAST);
                }
            }
        }

        private void handleInputComplete() {
            inputElements.emitComplete(FAIL_FAST);

            if (done.get()) return;
            inputComplete.compareAndSet(false, true);
            if (outputComplete.get()) {
                if (done.compareAndSet(false, true)) {
                    outputElements.emitComplete(FAIL_FAST);
                }
            }
        }

        private void handleNextInput(Object element) {
            inputElements.emitNext(element, FAIL_FAST);

            if (done.get()) return;
            inputProduced.compareAndSet(false, true);
            if (outputComplete.get()) {
                if (done.compareAndSet(false, true)) {
                    outputElements.emitComplete(FAIL_FAST);
                }
            }
        }
    }
}
