package io.art.rsocket.flux;

import io.art.entity.constants.EntityConstants.*;
import io.art.entity.immutable.Value;
import io.art.rsocket.model.*;
import io.rsocket.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import static io.art.rsocket.payload.RsocketPayloadReader.*;
import static java.util.Objects.*;
import java.util.concurrent.atomic.*;

public class RsocketRequestChannelEmitter {
    public RsocketRequestChannelEmitter(FluxSink<Payload> emitter, Publisher<Payload> inputPayloads, DataFormat dataFormat) {
        initializeEmitting(emitter, inputPayloads, dataFormat);
    }

    private static void initializeEmitting(FluxSink<Payload> emitter, Publisher<Payload> inputPayloads, DataFormat dataFormat) {
        EmitterProcessor<Value> serviceRequestProcessor = EmitterProcessor.create();
        FluxSink<Value> valueEmitter = serviceRequestProcessor.sink();
        AtomicBoolean subscribed = new AtomicBoolean(false);
        AtomicReference<Subscription> sourceSubscription = new AtomicReference<>();
        Flux.from(inputPayloads)
                .subscribe(payload -> {
                    RsocketPayloadValue payloadValue = readPayloadData(payload, dataFormat);
                    if (nonNull(payloadValue)) {
                        valueEmitter.next(payloadValue.getValue());
                    }
                }, error -> {
                    if (!subscribed.get()) {
                        emitter.error(error);
                        return;
                    }
                    valueEmitter.error(error);
                }, () -> {
                    if (!subscribed.get()) {
                        emitter.complete();
                        return;
                    }
                    valueEmitter.complete();
                }, subscription -> {
                    sourceSubscription.set(subscription);
                    subscription.request(1);
                });
    }
}
