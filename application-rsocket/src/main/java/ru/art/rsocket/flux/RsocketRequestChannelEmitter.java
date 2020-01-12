package ru.art.rsocket.flux;

import io.rsocket.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import ru.art.rsocket.constants.RsocketModuleConstants.*;
import ru.art.rsocket.model.*;
import ru.art.service.model.*;
import static java.util.Objects.*;
import static reactor.core.publisher.DirectProcessor.*;
import static ru.art.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.*;
import static ru.art.rsocket.model.RsocketRequestReactiveContext.*;
import static ru.art.rsocket.module.RsocketModule.*;
import static ru.art.rsocket.selector.RsocketDataFormatMimeTypeConverter.*;
import static ru.art.rsocket.writer.RsocketPayloadWriter.*;
import static ru.art.rsocket.writer.ServiceResponsePayloadWriter.*;
import static ru.art.service.ServiceController.*;
import static ru.art.service.factory.ServiceRequestFactory.*;
import java.util.concurrent.atomic.*;

public class RsocketRequestChannelEmitter {
    private final FluxSink<Payload> emitter;
    private final Publisher<Payload> inputPayloads;

    public RsocketRequestChannelEmitter(FluxSink<Payload> emitter, Publisher<Payload> inputPayloads) {
        this.emitter = emitter;
        this.inputPayloads = inputPayloads;
        initializeEmitting();
    }


    private void initializeEmitting() {
        RsocketDataFormat dataFormat = fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType());
        DirectProcessor<Object> processor = create();
        FluxSink<Object> sink = processor.serialize().sink();
        AtomicBoolean subscribed = new AtomicBoolean(false);
        from(inputPayloads)
                .map(payload -> fromPayload(payload, dataFormat))
                .subscribe(context -> {
                    if (context.isStopHandling()) {
                        if (nonNull(context.getAlternativeResponse())) {
                            emitter.next(writePayloadData(context.getAlternativeResponse(), dataFormat));
                            return;
                        }
                        emitter.complete();
                        return;
                    }
                    RsocketReactiveMethods rsocketReactiveMethods = context.getRsocketReactiveMethods();

                    if (isNull(rsocketReactiveMethods) || isNull(rsocketReactiveMethods.getRsocketMethod()) || isNull(rsocketReactiveMethods.getReactiveMethod())) {
                        emitter.complete();
                        return;
                    }

                    if (rsocketReactiveMethods.getReactiveMethod().requestProcessingMode() == STRAIGHT || rsocketReactiveMethods.getReactiveMethod().responseProcessingMode() == STRAIGHT) {
                        emitter.complete();
                        return;
                    }

                    if (!subscribed.get()) {
                        ServiceRequest<?> serviceRequest = newServiceRequest(context.getRsocketReactiveGroupKey().getServiceMethodCommand(), processor, rsocketReactiveMethods.getRsocketMethod().validationPolicy());
                        writeResponseReactive(rsocketReactiveMethods, executeServiceMethodUnchecked(serviceRequest), dataFormat)
                                .doOnSubscribe(subscription -> subscribed.set(true))
                                .subscribe(emitter::next, emitter::error, emitter::complete);
                        sink.next(context.getRequestData());
                        return;
                    }

                    sink.next(context.getRequestData());
                }, error -> {
                    if (subscribed.get()) {
                        sink.error(error);
                        return;
                    }
                    emitter.error(error);
                }, () -> {
                    if (subscribed.get()) {
                        sink.complete();
                        return;
                    }
                    emitter.complete();
                });
    }
}
