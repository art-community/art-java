package io.art.communicator.test.proxy;

import io.art.communicator.implementation.*;
import io.art.core.model.*;
import reactor.core.publisher.*;
import static io.art.communicator.test.registry.CommunicatorTestExecutionsRegistry.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.extensions.ReactiveExtensions.*;

public class TestCommunicatorImplementation implements CommunicatorActionImplementation {
    private CommunicatorActionIdentifier id;

    @Override
    public void initialize(CommunicatorActionIdentifier id) {
        this.id = id;
    }

    @Override
    public Flux<Object> communicate(Flux<Object> input) {
        register(id.getActionId(), orElse(blockFirst(input), new Object()));
        return Flux.just("test");
    }
}
