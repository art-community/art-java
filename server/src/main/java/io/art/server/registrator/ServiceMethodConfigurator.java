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

    private boolean logging;
    private boolean validatable;
    private boolean deactivating;

    public ServiceMethodConfigurator logging() {
        return logging(true);
    }

    public ServiceMethodConfigurator logging(boolean logging) {
        this.logging = logging;
        return this;
    }

    public ServiceMethodConfigurator validatable(boolean validatable) {
        this.validatable = validatable;
        return this;
    }

    public ServiceMethodConfigurator deactivating(boolean deactivating) {
        this.deactivating = deactivating;
        return this;
    }

    public UnaryOperator<ServiceMethodBuilder> configure() {
        UnaryOperator<ServiceMethodBuilder> decorator = UnaryOperator.identity();
        if (deactivating) {
            decorator = then(decorator, builder -> builder.inputDecorator(new ServiceDeactivationDecorator(id, configuration)));
        }
        if (logging) {
            decorator = then(decorator, builder -> builder.inputDecorator(new ServiceLoggingDecorator(id, configuration, INPUT)));
        }
        if (validatable) {
            decorator = then(decorator, builder -> builder.inputDecorator(new ServiceValidationDecorator(id, configuration)));
        }
        if (deactivating) {
            decorator = then(decorator, builder -> builder.outputDecorator(new ServiceDeactivationDecorator(id, configuration)));
        }
        if (logging) {
            decorator = then(decorator, builder -> builder.outputDecorator(new ServiceLoggingDecorator(id, configuration, OUTPUT)));
        }
        return decorator;
    }
}
