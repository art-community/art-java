package io.art.communicator.test.proxy;

import reactor.core.publisher.*;

public interface TestCommunicator {
    String m1(Flux<String> input);
}
