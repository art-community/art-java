package io.art.server.configurator;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.server.configuration.*;
import io.art.server.decorator.*;
import lombok.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.meta.constants.MetaConstants.MetaTypeModifiers.*;
import static io.art.server.method.ServiceMethod.*;
import static java.util.Objects.*;

@RequiredArgsConstructor
public class ServiceMethodConfigurator {
    private ServiceMethodIdentifier id;
    private final ServerConfiguration configuration;

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

    ServiceMethodBuilder configure(ServiceMethodBuilder builder, MetaType<?> inputType) {
        if (deactivable) {
            builder.inputDecorator(new ServiceDeactivationDecorator(id, configuration));
        }
        if (loggable) {
            builder.inputDecorator(new ServiceLoggingDecorator(id, configuration, INPUT));
        }
        if (validatable) {
            if (nonNull(inputType) && inputType.modifiers().contains(VALIDATABLE)) {
                builder.inputDecorator(new ServiceValidationDecorator(id, configuration));
            }
        }
        if (deactivable) {
            builder.outputDecorator(new ServiceDeactivationDecorator(id, configuration));
        }
        if (loggable) {
            builder.outputDecorator(new ServiceLoggingDecorator(id, configuration, OUTPUT));
        }
        return builder.id(id);
    }
}
