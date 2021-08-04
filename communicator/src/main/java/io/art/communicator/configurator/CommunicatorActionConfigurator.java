package io.art.communicator.configurator;

import io.art.communicator.action.CommunicatorAction.*;
import io.art.communicator.configuration.*;
import io.art.communicator.decorator.*;
import io.art.core.model.*;
import lombok.*;
import static io.art.core.constants.MethodDecoratorScope.*;

@RequiredArgsConstructor
public class CommunicatorActionConfigurator {
    private final CommunicatorActionIdentifier id;
    private final CommunicatorConfiguration configuration;

    private boolean loggable;
    private boolean deactivable = true;
    private boolean resilience = false;

    public CommunicatorActionConfigurator loggable() {
        return loggable(true);
    }

    public CommunicatorActionConfigurator loggable(boolean loggable) {
        this.loggable = loggable;
        return this;
    }

    public CommunicatorActionConfigurator resilience() {
        return resilience(true);
    }

    public CommunicatorActionConfigurator resilience(boolean resilience) {
        this.resilience = resilience;
        return this;
    }

    public CommunicatorActionConfigurator deactivable(boolean deactivable) {
        this.deactivable = deactivable;
        return this;
    }

    CommunicatorActionBuilder configure(CommunicatorActionBuilder builder) {
        if (deactivable) {
            builder.inputDecorator(new CommunicatorDeactivationDecorator(id, configuration));
        }
        if (loggable) {
            builder.inputDecorator(new CommunicatorLoggingDecorator(id, configuration, INPUT));
        }
        if (resilience) {
            builder.inputDecorator(new CommunicatorResilienceDecorator(id, configuration));
        }
        if (deactivable) {
            builder.outputDecorator(new CommunicatorDeactivationDecorator(id, configuration));
        }
        if (loggable) {
            builder.outputDecorator(new CommunicatorLoggingDecorator(id, configuration, OUTPUT));
        }
        return builder;
    }
}
