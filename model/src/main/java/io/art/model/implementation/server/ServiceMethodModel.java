package io.art.model.implementation.server;

import static io.art.server.specification.ServiceMethodSpecification.*;
import java.util.function.*;

public interface ServiceMethodModel {
    String getId();

    Function<ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> getDecorator();

    default ServiceMethodSpecificationBuilder implement(ServiceMethodSpecificationBuilder builder) {
        return getDecorator().apply(builder);
    }

}
