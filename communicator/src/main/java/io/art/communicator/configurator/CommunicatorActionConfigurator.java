package io.art.communicator.configurator;

import io.art.communicator.configuration.*;
import io.art.communicator.configuration.CommunicatorActionConfiguration.*;
import io.art.core.annotation.*;
import io.art.resilience.configuration.*;
import io.art.resilience.configuration.ResilienceConfiguration.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import static java.util.Objects.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@ForUsing
public class CommunicatorActionConfigurator {
    private boolean logging = false;
    private boolean deactivated = false;
    private UnaryOperator<ResilienceConfigurationBuilder> resilience;
    private final List<UnaryOperator<Flux<Object>>> inputDecorators = linkedList();
    private final List<UnaryOperator<Flux<Object>>> outputDecorators = linkedList();

    public CommunicatorActionConfigurator logging() {
        return logging(true);
    }

    public CommunicatorActionConfigurator logging(boolean loggable) {
        this.logging = loggable;
        return this;
    }

    public CommunicatorActionConfigurator resilience() {
        return resilience(identity());
    }

    public CommunicatorActionConfigurator resilience(UnaryOperator<ResilienceConfigurationBuilder> resilience) {
        this.resilience = resilience;
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

    CommunicatorActionConfiguration configure(CommunicatorActionConfiguration configuration) {
        CommunicatorActionConfigurationBuilder builder = configuration.toBuilder()
                .deactivated(deactivated)
                .inputDecorators(immutableArrayOf(inputDecorators))
                .outputDecorators(immutableArrayOf(outputDecorators))
                .logging(logging);
        if (nonNull(resilience)) {
            builder.resilience(resilience.apply(ResilienceConfiguration.builder()).build());
        }
        return builder.build();
    }
}
