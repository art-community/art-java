package ru.art.rsocket.interceptor;

import io.rsocket.*;
import ru.art.entity.*;
import java.util.function.*;


@FunctionalInterface
public interface RsocketPayloadValueInterceptor extends BiFunction<RSocket, Value, RSocket> {
    static RsocketPayloadValueInterceptor intercept(BiConsumer<Value, RSocket> interception) {
        return (rsocket, value) -> {
            interception.accept(value, rsocket);
            return rsocket;
        };
    }

    static RsocketPayloadValueInterceptor intercept(Consumer<Value> interception) {
        return (rsocket, value) -> {
            interception.accept(value);
            return rsocket;
        };
    }
}
