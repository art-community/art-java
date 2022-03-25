package io.art.communicator.configurator;

import io.art.communicator.configuration.*;
import io.art.core.annotation.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import java.util.*;
import java.util.function.*;

@Public
public class CommunicatorActionConfigurator {
    private Boolean logging;
    private Boolean deactivated;
    private final List<UnaryOperator<Flux<Object>>> inputDecorators = linkedList();
    private final List<UnaryOperator<Flux<Object>>> outputDecorators = linkedList();

    public CommunicatorActionConfigurator logging() {
        return logging(true);
    }

    public CommunicatorActionConfigurator logging(boolean loggable) {
        this.logging = loggable;
        return this;
    }

    public CommunicatorActionConfigurator deactivated(boolean deactivated) {
        this.deactivated = deactivated;
        return this;
    }

    public <T> CommunicatorActionConfigurator decorateInput(UnaryOperator<Flux<T>> decorator) {
        inputDecorators.add(cast(decorator));
        return this;
    }

    public <T> CommunicatorActionConfigurator decorateOutput(UnaryOperator<Flux<T>> decorator) {
        outputDecorators.add(cast(decorator));
        return this;
    }

    CommunicatorActionConfiguration createConfiguration(CommunicatorActionConfiguration configuration) {
        return configuration.toBuilder()
                .deactivated(deactivated)
                .inputDecorators(immutableArrayOf(inputDecorators))
                .outputDecorators(immutableArrayOf(outputDecorators))
                .logging(logging)
                .build();
    }
}
