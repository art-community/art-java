package io.art.communicator.configurator;

import io.art.communicator.action.CommunicatorAction.*;
import io.art.communicator.configuration.*;
import io.art.communicator.decorator.*;
import io.art.core.model.*;
import lombok.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.extensions.FunctionExtensions.*;
import java.util.function.*;

@RequiredArgsConstructor
public class CommunicatorActionConfigurator {
    private final CommunicatorActionIdentifier id;
    private final CommunicatorConfiguration configuration;

    private String connector;
    private boolean loggable;
    private boolean deactivable = true;

    public CommunicatorActionConfigurator to(String connector) {
        this.connector = connector;
        return this;
    }

    public CommunicatorActionConfigurator loggable() {
        return loggable(true);
    }

    public CommunicatorActionConfigurator loggable(boolean loggable) {
        this.loggable = loggable;
        return this;
    }

    public CommunicatorActionConfigurator deactivable(boolean deactivable) {
        this.deactivable = deactivable;
        return this;
    }

    UnaryOperator<CommunicatorActionBuilder> configure() {
        UnaryOperator<CommunicatorActionBuilder> decorator = UnaryOperator.identity();
        if (deactivable) {
            decorator = then(decorator, builder -> builder.inputDecorator(new CommunicatorDeactivationDecorator(id, configuration)));
        }
        if (loggable) {
            decorator = then(decorator, builder -> builder.inputDecorator(new CommunicatorLoggingDecorator(id, configuration, INPUT)));
        }
        if (deactivable) {
            decorator = then(decorator, builder -> builder.outputDecorator(new CommunicatorDeactivationDecorator(id, configuration)));
        }
        if (loggable) {
            decorator = then(decorator, builder -> builder.outputDecorator(new CommunicatorLoggingDecorator(id, configuration, OUTPUT)));
        }
        if (isNotEmpty(connector)) {
            decorator = then(decorator, builder -> builder.connector(connector));
        }
        return decorator;
    }
}
