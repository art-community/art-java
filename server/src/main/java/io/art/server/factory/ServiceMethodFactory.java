package io.art.server.factory;

import io.art.core.model.*;
import io.art.meta.invoker.*;
import io.art.meta.model.*;
import io.art.server.decorator.*;
import io.art.server.method.*;
import io.art.server.method.ServiceMethod.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;

@UtilityClass
public class ServiceMethodFactory {
    public ServiceMethod serviceMethod(MetaClass<?> owner, MetaMethod<?> method) {
        return serviceMethod(serviceMethodId(owner.definition().type().getSimpleName(), method.name()), owner, method);
    }

    public ServiceMethod serviceMethod(ServiceMethodIdentifier id, MetaClass<?> owner, MetaMethod<?> method) {
        ServiceMethodBuilder builder = ServiceMethod.builder()
                .id(id)
                .outputType(method.returnType())
                .invoker(new MetaMethodInvoker(owner, method))
                .inputDecorator(new ServiceStateDecorator(id))
                .inputDecorator(new ServiceDeactivationDecorator(id))
                .inputDecorator(new ServiceValidationDecorator(id))
                .inputDecorator(new ServiceLoggingDecorator(id, INPUT))
                .outputDecorator(new ServiceValidationDecorator(id))
                .outputDecorator(new ServiceLoggingDecorator(id, OUTPUT));
        if (isNotEmpty(method.parameters())) {
            return builder.inputType(immutableArrayOf(method.parameters().values()).get(0).type()).build();
        }
        return builder.build();
    }
}
