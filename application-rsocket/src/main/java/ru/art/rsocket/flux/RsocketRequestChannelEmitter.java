package ru.art.rsocket.flux;

import io.rsocket.*;
import org.reactivestreams.*;
import reactor.core.*;
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
    public RsocketRequestChannelEmitter(FluxSink<Payload> emitter, Publisher<Payload> inputPayloads) {
        initializeEmitting(emitter, inputPayloads);
    }

    private static void initializeEmitting(FluxSink<Payload> emitter, Publisher<Payload> inputPayloads) {
        RsocketDataFormat dataFormat = fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType());
        DirectProcessor<Object> processor = create();
        FluxSink<Object> sink = processor.sink();
        AtomicBoolean subscribed = new AtomicBoolean(false);
        AtomicReference<Subscription> sourceSubscription = new AtomicReference<>();
        AtomicReference<RsocketReactiveMethods> rsocketReactiveMethods = new AtomicReference<>();
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

                    if (isNull(rsocketReactiveMethods.get())) {
                        RsocketReactiveMethods methods = context.getRsocketReactiveMethods();
                        if (isNull(methods) || isNull(methods.getRsocketMethod()) || isNull(methods.getReactiveMethod())) {
                            emitter.complete();
                            return;
                        }

                        if (methods.getReactiveMethod().requestProcessingMode() == STRAIGHT || methods.getReactiveMethod().responseProcessingMode() == STRAIGHT) {
                            emitter.complete();
                            return;
                        }

                        rsocketReactiveMethods.set(methods);
                    }

                    if (!subscribed.get()) {
                        ServiceRequest<?> serviceRequest = newServiceRequest(context.getRsocketReactiveGroupKey().getServiceMethodCommand(), processor
                                .doOnSubscribe(subscription -> subscribed.set(true))
                                .doOnCancel(() -> sourceSubscription.get().cancel())
                                .doOnRequest(count -> sourceSubscription.get().request(count)), rsocketReactiveMethods.get().getRsocketMethod().validationPolicy());
                        Disposable serviceResponseDisposable = writeResponseReactive(rsocketReactiveMethods.get(), executeServiceMethodUnchecked(serviceRequest), dataFormat)
                                .subscribe(emitter::next, emitter::error, emitter::complete, subscription -> {
                                    emitter.onRequest(subscription::request);
                                    emitter.onCancel(subscription::cancel);
                                });
                        emitter.onDispose(serviceResponseDisposable);
                        sink.next(context.getRequestData());
                        return;
                    }
                    sink.next(context.getRequestData());
                }, error -> {
                    if (!subscribed.get()) {
                        emitter.error(error);
                        return;
                    }
                    sink.error(error);
                }, () -> {
                    if (!subscribed.get()) {
                        emitter.complete();
                        return;
                    }
                    sink.complete();
                }, subscription -> {
                    sourceSubscription.set(subscription);
                    subscription.request(1);
                });
    }
}
