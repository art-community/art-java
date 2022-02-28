package io.art.tarantool.module;

import io.art.core.annotation.*;
import io.art.server.configuration.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import java.util.*;
import java.util.function.*;

@Public
public class TarantoolSubscriptionConfigurator {
    private Boolean logging;
    private Boolean deactivated;
    private boolean validating = true;
    private final List<UnaryOperator<Flux<Object>>> inputDecorators = linkedList();

    public TarantoolSubscriptionConfigurator logging() {
        return logging(true);
    }

    public TarantoolSubscriptionConfigurator logging(boolean logging) {
        this.logging = logging;
        return this;
    }

    public TarantoolSubscriptionConfigurator validating() {
        return validating(true);
    }

    public TarantoolSubscriptionConfigurator validating(boolean validating) {
        this.validating = validating;
        return this;
    }

    public TarantoolSubscriptionConfigurator deactivated() {
        return deactivated(true);
    }

    public TarantoolSubscriptionConfigurator deactivated(boolean deactivated) {
        this.deactivated = deactivated;
        return this;
    }

    public <T> TarantoolSubscriptionConfigurator decorateInput(UnaryOperator<Flux<T>> decorator) {
        inputDecorators.add(cast(decorator));
        return this;
    }

    ServiceMethodConfiguration configure(ServiceMethodConfiguration configuration) {
        return configuration.toBuilder()
                .deactivated(deactivated)
                .logging(logging)
                .validating(validating)
                .inputDecorators(immutableArrayOf(inputDecorators))
                .build();
    }
}
