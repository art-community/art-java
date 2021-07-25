package io.art.communicator.test.proxy;

import reactor.core.publisher.*;

public interface TestCommunicator {
    void m0(Flux<String> input);
    void m1();
    String m2(Flux<String> input);

    String m3();

    String m3(String v1, String v2);
}
