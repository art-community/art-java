package io.art.server.configurator;

import io.art.core.annotation.*;
import io.art.server.configuration.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import java.util.*;
import java.util.function.*;

@ForUsing
public class ServiceMethodConfigurator {
    private boolean logging = false;
    private boolean validating = true;
    private boolean deactivated = false;
    private final List<UnaryOperator<Flux<Object>>> inputDecorators = linkedList();
    private final List<UnaryOperator<Flux<Object>>> outputDecorators = linkedList();

    public ServiceMethodConfigurator logging() {
        return logging(true);
    }

    public ServiceMethodConfigurator logging(boolean logging) {
        this.logging = logging;
        return this;
    }

    public ServiceMethodConfigurator validating() {
        return validating(true);
    }

    public ServiceMethodConfigurator validating(boolean validating) {
        this.validating = validating;
        return this;
    }

    public ServiceMethodConfigurator deactivated() {
        return deactivated(true);
    }

    public ServiceMethodConfigurator deactivated(boolean deactivated) {
        this.deactivated = deactivated;
        return this;
    }

    public <T> ServiceMethodConfigurator decorateInput(UnaryOperator<Flux<T>> decorator) {
        inputDecorators.add(cast(decorator));
        return this;
    }

    public <T> ServiceMethodConfigurator decorateOutput(UnaryOperator<Flux<T>> decorator) {
        outputDecorators.add(cast(decorator));
        return this;
    }

    ServiceMethodConfiguration configure(ServiceMethodConfiguration configuration) {
        return configuration.toBuilder()
                .deactivated(deactivated)
                .logging(logging)
                .validating(validating)
                .inputDecorators(immutableArrayOf(inputDecorators))
                .outputDecorators(immutableArrayOf(outputDecorators))
                .build();
    }
}
