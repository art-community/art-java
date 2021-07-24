package io.art.server.factory;

import io.art.meta.invoker.*;
import io.art.meta.model.*;
import io.art.server.specification.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.factory.ArrayFactory.*;

@UtilityClass
public class ServiceMethodSpecificationFactory {
    public ServiceMethodSpecification forMethod(MetaClass<?> owner, MetaMethod<?> method) {
        ServiceMethodSpecification.ServiceMethodSpecificationBuilder builder = ServiceMethodSpecification.builder()
                .outputType(method.returnType())
                .method(new MetaMethodInvoker(owner, method));
        if (isNotEmpty(method.parameters())) {
            return builder.inputType(immutableArrayOf(method.parameters().values()).get(0).type()).build();
        }
        return builder.build();
    }
}
