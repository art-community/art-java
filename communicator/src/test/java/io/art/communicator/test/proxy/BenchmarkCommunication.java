package io.art.communicator.test.proxy;

import io.art.communicator.*;
import reactor.core.publisher.*;

public class BenchmarkCommunication implements Communication {
    @Override
    public Flux<Object> communicate(Flux<Object> input) {
        return Flux.empty();
    }
}
