package io.art.tarantool.model;

import io.art.meta.model.*;
import io.art.server.method.*;
import io.art.tarantool.descriptor.*;
import org.msgpack.value.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static java.util.Objects.*;
import java.util.concurrent.atomic.*;

public class TarantoolSubscription {
    private final Sinks.Many<Object> sink;
    private final ServiceMethod method;
    private MetaType<?> inputType;
    private final AtomicReference<Subscription> subscription = new AtomicReference<>();

    public TarantoolSubscription(Sinks.Many<Object> sink, ServiceMethod method) {
        this.sink = sink;
        this.method = method;
        inputType = method.getInputType();
        if (nonNull(inputType)) {
            if (inputType.internalKind() == FLUX || inputType.internalKind() == MONO) {
                inputType = inputType.parameters().get(0);
            }
        }
        method.serve(sink.asFlux().doOnSubscribe(this.subscription::set));
    }

    public void publish(Value request, TarantoolModelReader reader) {
        if (isNull(request)) {
            method.serve(Flux.empty());
            return;
        }
        sink.tryEmitNext(reader.read(inputType, request));
    }

    public void cancel() {
        apply(subscription.get(), Subscription::cancel);
    }
}
