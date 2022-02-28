package io.art.tarantool.model;

import io.art.server.method.*;
import io.art.tarantool.descriptor.*;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static java.util.Objects.*;
import java.util.*;

public class TarantoolSubscription {
    private final Sinks.Many<Object> sink;
    private final ServiceMethod method;

    public TarantoolSubscription(Sinks.Many<Object> sink, ServiceMethod method) {
        this.sink = sink;
        this.method = method;
        method.serve(sink.asFlux());
    }

    public void publish(Value payload, TarantoolModelReader reader) {
        if (isNull(payload) || !payload.isMapValue()) {
            return;
        }
        Map<org.msgpack.value.Value, org.msgpack.value.Value> mapValue = payload.asMapValue().map();
        org.msgpack.value.Value serviceId = mapValue.get(SERVICE_ID_KEY);
        org.msgpack.value.Value methodId = mapValue.get(METHOD_ID_KEY);
        Value request = mapValue.get(SERVICE_METHOD_REQUEST_KEY);
        if (isNull(serviceId) || !serviceId.isStringValue()) {
            return;
        }
        if (isNull(methodId) || !methodId.isStringValue()) {
            return;
        }
        sink.tryEmitNext(reader.read(method.getInputType(), request));
    }

    public void cancel() {
        sink.tryEmitComplete();
    }
}
