package io.art.rsocket.test.service;

import io.art.rsocket.test.communicator.*;
import reactor.core.publisher.*;
import static io.art.core.extensions.ReactiveExtensions.*;

public class TestRsocketService implements TestRsocket {
    public void m1(String input) {
        System.out.println(input);
    }

    @Override
    public void m2(Mono<String> input) {
        System.out.println(block(input));
    }
}
