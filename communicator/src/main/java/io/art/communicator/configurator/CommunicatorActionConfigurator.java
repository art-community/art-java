package io.art.communicator.configurator;

import io.art.communicator.action.CommunicatorAction.*;
import io.art.communicator.configuration.*;
import io.art.communicator.decorator.*;
import io.art.core.model.*;
import lombok.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.extensions.FunctionExtensions.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import java.util.function.*;

@RequiredArgsConstructor
public class CommunicatorActionConfigurator {
    private final CommunicatorActionIdentifier id;
    private final CommunicatorConfiguration configuration;

    private String targetServiceId;
    private String targetMethodId;
    private boolean loggable;
    private boolean deactivable = true;
    private boolean resilience = false;

    public CommunicatorActionConfigurator target(String serviceId, String methodId) {
        this.targetServiceId = serviceId;
        this.targetMethodId = methodId;
        return this;
    }

    public CommunicatorActionConfigurator target(String serviceId) {
        this.targetServiceId = serviceId;
        return this;
    }

    public CommunicatorActionConfigurator target(Class<?> targetClass) {
        this.targetServiceId = asId(targetClass);
        return this;
    }

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

    UnaryOperator<CommunicatorActionBuilder> configure() {
        UnaryOperator<CommunicatorActionBuilder> decorator = UnaryOperator.identity();
        if (deactivable) {
            decorator = then(decorator, builder -> builder.inputDecorator(new CommunicatorDeactivationDecorator(id, configuration)));
        }
        if (loggable) {
            decorator = then(decorator, builder -> builder.inputDecorator(new CommunicatorLoggingDecorator(id, configuration, INPUT)));
        }
        if (resilience) {
            decorator = then(decorator, builder -> builder.inputDecorator(new CommunicatorResilienceDecorator(id, configuration)));
        }
        if (deactivable) {
            decorator = then(decorator, builder -> builder.outputDecorator(new CommunicatorDeactivationDecorator(id, configuration)));
        }
        if (loggable) {
            decorator = then(decorator, builder -> builder.outputDecorator(new CommunicatorLoggingDecorator(id, configuration, OUTPUT)));
        }
        if (isNotEmpty(targetServiceId)) {
            decorator = then(decorator, builder -> builder.targetServiceMethod(serviceMethodId(targetServiceId, builder.build().getId().getActionId())));
        }
        if (isNotEmpty(targetServiceId) && isNotEmpty(targetMethodId)) {
            decorator = then(decorator, builder -> builder.targetServiceMethod(serviceMethodId(targetServiceId, targetMethodId)));
        }
        return decorator;
    }
}
