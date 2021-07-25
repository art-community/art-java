package io.art.communicator.test.proxy;

import io.art.communicator.implementation.*;
import reactor.core.publisher.*;

public class BenchmarkCommunicatorImplementation implements CommunicatorActionImplementation {
    @Override
    public Flux<Object> communicate(Flux<Object> input) {
        return Flux.empty();
    }
}
