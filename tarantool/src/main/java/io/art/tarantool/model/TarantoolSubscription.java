package io.art.tarantool.model;

import io.art.server.method.*;
import io.art.tarantool.descriptor.*;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static java.util.Objects.*;

public class TarantoolSubscription {
    private final Sinks.Many<Object> sink;
    private final ServiceMethod method;

    public TarantoolSubscription(Sinks.Many<Object> sink, ServiceMethod method) {
        this.sink = sink;
        this.method = method;
        method.serve(sink.asFlux());
    }

    public void publish(Value request, TarantoolModelReader reader) {
        if (isNull(request)) {
            method.serve(Flux.empty());
            return;
        }
        sink.tryEmitNext(reader.read(method.getInputType(), request));
    }

    public void cancel() {
        sink.tryEmitComplete();
    }
}
