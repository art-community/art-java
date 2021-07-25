package io.art.communicator.test.proxy;

import io.art.communicator.implementation.*;
import reactor.core.publisher.*;

public class TestCommunicatorImplementation implements CommunicatorActionImplementation {
    public void m1() {

    }

    @Override
    public Flux<Object> communicate(Flux<Object> input) {
        return null;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void dispose() {

    }
}
