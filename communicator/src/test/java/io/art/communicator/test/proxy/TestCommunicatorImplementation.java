package io.art.communicator.test.proxy;

import io.art.communicator.implementation.*;
import reactor.core.publisher.*;

public class TestCommunicatorImplementation implements CommunicatorActionImplementation {
    @Override
    public Flux<Object> communicate(Flux<Object> input) {
        return Flux.just("test");
    }

    @Override
    public void initialize() {

    }

    @Override
    public void dispose() {

    }
}
