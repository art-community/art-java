package io.art.server.factory;

import io.art.core.model.*;
import io.art.meta.invoker.*;
import io.art.meta.model.*;
import io.art.server.decorator.*;
import io.art.server.method.*;
import io.art.server.method.ServiceMethod.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.meta.constants.MetaConstants.MetaTypeModifiers.*;
import static java.util.Objects.*;

@UtilityClass
public class ServiceMethodFactory {
    public ServiceMethod serviceMethod(MetaClass<?> owner, MetaMethod<?> method) {
        return serviceMethod(serviceMethodId(owner.definition().type().getSimpleName(), method.name()), owner, method);
    }

    public ServiceMethod serviceMethod(ServiceMethodIdentifier id, MetaClass<?> owner, MetaMethod<?> method) {
        MetaType<?> inputType = orNull(() -> immutableArrayOf(method.parameters().values()).get(0).type(), isNotEmpty(method.parameters()));
        boolean validatable = nonNull(inputType) && inputType.modifiers().contains(VALIDATABLE);
        ServiceMethodBuilder builder = ServiceMethod.builder()
                .id(id)
                .outputType(method.returnType())
                .invoker(new MetaMethodInvoker(owner, method))
                .inputDecorator(new ServiceStateDecorator(id))
                .inputDecorator(new ServiceDeactivationDecorator(id))
                .inputDecorator(new ServiceValidationDecorator(id, validatable))
                .inputDecorator(new ServiceLoggingDecorator(id, INPUT))
                .outputDecorator(new ServiceValidationDecorator(id, validatable))
                .outputDecorator(new ServiceLoggingDecorator(id, OUTPUT));
        if (nonNull(inputType)) {
            return builder.inputType(inputType).build();
        }
        return builder.build();
    }
}
