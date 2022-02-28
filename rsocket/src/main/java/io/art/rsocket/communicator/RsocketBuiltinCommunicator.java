package io.art.rsocket.communicator;

import io.art.communicator.*;
import reactor.core.publisher.*;

public interface RsocketBuiltinCommunicator extends Communicator {
    void fireAndForget(Mono<byte[]> input);

    Mono<byte[]> requestResponse(Mono<byte[]> input);

    Flux<byte[]> requestStream(Mono<byte[]> input);

    Flux<byte[]> requestChannel(Flux<byte[]> input);
}
