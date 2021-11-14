package io.art.communicator.test.proxy;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import reactor.core.publisher.*;
import static io.art.communicator.test.registry.CommunicatorTestExecutionsRegistry.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.extensions.ReactiveExtensions.*;

public class TestCommunication implements Communication {
    private CommunicatorAction action;

    @Override
    public void initialize(CommunicatorAction action) {
        this.action = action;
    }

    @Override
    public Flux<Object> communicate(Flux<Object> input) {
        register(action.getId().getActionId(), orElse(blockFirst(input), new Object()));
        return Flux.just("test");
    }
}
