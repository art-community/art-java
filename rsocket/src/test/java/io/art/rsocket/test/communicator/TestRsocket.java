package io.art.rsocket.test.communicator;

import io.art.communicator.model.*;
import reactor.core.publisher.*;

public interface TestRsocket extends Communicator {
    void m1(String input);

    void m2(Mono<String> input);
}
