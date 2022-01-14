package io.art.server.factory;

import io.art.core.model.*;
import io.art.meta.invoker.*;
import io.art.meta.model.*;
import io.art.server.configuration.*;
import io.art.server.decorator.*;
import io.art.server.method.*;
import io.art.server.method.ServiceMethod.*;
import io.art.server.refresher.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.meta.constants.MetaConstants.MetaTypeModifiers.*;
import static java.util.Objects.*;

@UtilityClass
public class ServiceMethodFactory {
    public ServiceMethod preconfiguredServiceMethod(MetaClass<?> owner, MetaMethod<?> method) {
        return preconfiguredServiceMethod(serviceMethodId(owner, method), owner, method);
    }

    public ServiceMethod serviceMethod(MetaClass<?> owner, MetaMethod<?> method) {
        return serviceMethod(serviceMethodId(owner, method), owner, method);
    }

    public ServiceMethodIdentifier serviceMethodId(MetaClass<?> owner, MetaMethod<?> method) {
        return ServiceMethodIdentifier.serviceMethodId(idByDash(owner.definition().type()), method.name());
    }

    public ServiceMethod serviceMethod(ServiceMethodIdentifier id, MetaClass<?> owner, MetaMethod<?> method) {
        MetaType<?> inputType = orNull(() -> immutableArrayOf(method.parameters().values()).get(0).type(), isNotEmpty(method.parameters()));
        ServiceMethodBuilder builder = ServiceMethod.builder()
                .id(id)
                .outputType(method.returnType())
                .invoker(new MetaMethodInvoker(owner, method));
        if (nonNull(inputType)) {
            return builder.inputType(inputType).build();
        }
        return builder.build();
    }

    public ServiceMethod preconfiguredServiceMethod(ServiceMethodIdentifier id, MetaClass<?> owner, MetaMethod<?> method) {
        MetaType<?> inputType = orNull(() -> immutableArrayOf(method.parameters().values()).get(0).type(), isNotEmpty(method.parameters()));
        boolean validatable = nonNull(inputType) && inputType.modifiers().contains(VALIDATABLE);
        ServerConfiguration configuration = ServerConfiguration.serverConfiguration(new ServerRefresher());
        ServiceMethodBuilder builder = ServiceMethod.builder()
                .id(id)
                .outputType(method.returnType())
                .invoker(new MetaMethodInvoker(owner, method))
                .inputDecorator(new ServiceDeactivationDecorator(id, configuration));

        if (validatable) {
            builder.inputDecorator(new ServiceValidationDecorator(id, configuration));
        }

        if (withLogging()) {
            builder.inputDecorator(new ServiceLoggingDecorator(id, configuration, INPUT));
        }
        builder.outputDecorator(new ServiceDeactivationDecorator(id, configuration));

        if (withLogging()) {
            builder.outputDecorator(new ServiceLoggingDecorator(id, configuration, OUTPUT));
        }

        if (nonNull(inputType)) {
            return builder.inputType(inputType).build();
        }
        return builder.build();
    }
}
