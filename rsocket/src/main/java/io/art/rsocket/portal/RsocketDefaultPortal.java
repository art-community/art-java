package io.art.rsocket.portal;

import io.art.communicator.*;
import reactor.core.publisher.*;

public interface RsocketDefaultPortal extends Portal {
    interface RsocketExecutionCommunicator extends Communicator {
        void fireAndForget(Mono<byte[]> input);

        Mono<byte[]> requestResponse(Mono<byte[]> input);

        Flux<byte[]> requestStream(Mono<byte[]> input);

        Flux<byte[]> requestChannel(Flux<byte[]> input);
    }
}
