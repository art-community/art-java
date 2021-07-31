package io.art.server.registrator;

import io.art.core.model.*;
import io.art.server.configuration.*;
import io.art.server.decorator.*;
import lombok.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.extensions.FunctionExtensions.*;
import static io.art.server.method.ServiceMethod.*;
import java.util.function.*;

@RequiredArgsConstructor
public class ServiceMethodConfigurator {
    private final ServiceMethodIdentifier id;
    private final ServerConfiguration configuration;

    private boolean loggable;
    private boolean validatable = true;
    private boolean deactivable = true;

    public ServiceMethodConfigurator loggable() {
        return loggable(true);
    }

    public ServiceMethodConfigurator loggable(boolean loggable) {
        this.loggable = loggable;
        return this;
    }

    public ServiceMethodConfigurator validatable(boolean validatable) {
        this.validatable = validatable;
        return this;
    }

    public ServiceMethodConfigurator deactivable(boolean deactivable) {
        this.deactivable = deactivable;
        return this;
    }

    UnaryOperator<ServiceMethodBuilder> configure() {
        UnaryOperator<ServiceMethodBuilder> decorator = UnaryOperator.identity();
        if (deactivable) {
            decorator = then(decorator, builder -> builder.inputDecorator(new ServiceDeactivationDecorator(id, configuration)));
        }
        if (loggable) {
            decorator = then(decorator, builder -> builder.inputDecorator(new ServiceLoggingDecorator(id, configuration, INPUT)));
        }
        if (validatable) {
            decorator = then(decorator, builder -> builder.inputDecorator(new ServiceValidationDecorator(id, configuration)));
        }
        if (deactivable) {
            decorator = then(decorator, builder -> builder.outputDecorator(new ServiceDeactivationDecorator(id, configuration)));
        }
        if (loggable) {
            decorator = then(decorator, builder -> builder.outputDecorator(new ServiceLoggingDecorator(id, configuration, OUTPUT)));
        }
        return decorator;
    }
}
