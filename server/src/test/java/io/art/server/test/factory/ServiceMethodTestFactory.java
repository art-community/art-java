package io.art.server.test.factory;

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
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.meta.constants.MetaConstants.MetaTypeModifiers.*;
import static java.util.Objects.*;

@UtilityClass
public class ServiceMethodTestFactory {
    public ServiceMethod serviceMethod(MetaClass<?> owner, MetaMethod<?> method) {
        return serviceMethod(serviceMethodId(owner.definition().type().getSimpleName(), method.name()), owner, method);
    }

    public ServiceMethod serviceMethod(ServiceMethodIdentifier id, MetaClass<?> owner, MetaMethod<?> method) {
        MetaType<?> inputType = orNull(() -> immutableArrayOf(method.parameters().values()).get(0).type(), isNotEmpty(method.parameters()));
        boolean validatable = nonNull(inputType) && inputType.modifiers().contains(VALIDATABLE);
        ServerConfiguration configuration = ServerConfiguration.builder().refresher(new ServerRefresher()).build();
        ServiceMethodBuilder builder = ServiceMethod.builder()
                .id(id)
                .outputType(method.returnType())
                .invoker(new MetaMethodInvoker(owner, method))
                .inputDecorator(new ServiceDeactivationDecorator(id, configuration));

        if (validatable) {
            builder.inputDecorator(new ServiceValidationDecorator(id, configuration));
        }

        builder.inputDecorator(new ServiceLoggingDecorator(id, configuration, INPUT))
                .outputDecorator(new ServiceDeactivationDecorator(id, configuration))
                .outputDecorator(new ServiceLoggingDecorator(id, configuration, OUTPUT));
        if (nonNull(inputType)) {
            return builder.inputType(inputType).build();
        }
        return builder.build();
    }
}
