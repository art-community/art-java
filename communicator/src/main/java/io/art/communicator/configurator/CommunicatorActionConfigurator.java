package io.art.communicator.configurator;

import io.art.communicator.configuration.*;
import io.art.communicator.configuration.CommunicatorActionConfiguration.*;
import io.art.resilience.configuration.*;
import io.art.resilience.configuration.ResilienceConfiguration.*;
import static java.util.Objects.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

public class CommunicatorActionConfigurator {
    private boolean logging = false;
    private boolean deactivated = false;
    private UnaryOperator<ResilienceConfigurationBuilder> resilience;

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

    CommunicatorActionConfiguration configure(CommunicatorActionConfiguration configuration) {
        CommunicatorActionConfigurationBuilder builder = configuration.toBuilder()
                .deactivated(deactivated)
                .logging(logging);
        if (nonNull(resilience)) {
            builder.resilience(resilience.apply(ResilienceConfiguration.builder()).build());
        }
        return builder.build();
    }
}
