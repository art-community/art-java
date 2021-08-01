package io.art.server.configurator;

import io.art.core.model.*;
import io.art.server.configuration.*;
import io.art.server.decorator.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.extensions.FunctionExtensions.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.meta.constants.MetaConstants.MetaTypeModifiers.*;
import static io.art.server.method.ServiceMethod.*;
import java.util.function.*;

public class ServiceMethodConfigurator {
    private ServiceMethodIdentifier id;
    private final ServerConfiguration configuration;

    ServiceMethodConfigurator(ServiceMethodIdentifier id, ServerConfiguration configuration) {
        this.id = id;
        this.configuration = configuration;
    }

    private boolean loggable;
    private boolean validatable = true;
    private boolean deactivable = true;

    public ServiceMethodConfigurator serviceId(String id) {
        this.id = serviceMethodId(id, this.id.getMethodId());
        return this;
    }

    public ServiceMethodConfigurator methodId(String id) {
        this.id = serviceMethodId(this.id.getServiceId(), id);
        return this;
    }

    public ServiceMethodConfigurator id(String serviceId, String methodId) {
        this.id = serviceMethodId(serviceId, methodId);
        return this;
    }

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
            decorator = then(decorator, builder -> applyIf(
                    builder,
                    validatable -> builder.build().getInputType().modifiers().contains(VALIDATABLE),
                    validatable -> validatable.inputDecorator(new ServiceValidationDecorator(id, configuration))
            ));
        }
        if (deactivable) {
            decorator = then(decorator, builder -> builder.outputDecorator(new ServiceDeactivationDecorator(id, configuration)));
        }
        if (loggable) {
            decorator = then(decorator, builder -> builder.outputDecorator(new ServiceLoggingDecorator(id, configuration, OUTPUT)));
        }
        decorator = then(decorator, builder -> builder.id(id));
        return decorator;
    }
}
