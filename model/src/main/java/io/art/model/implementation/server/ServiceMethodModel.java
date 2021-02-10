package io.art.model.implementation.server;

import io.art.model.implementation.communicator.*;
import static io.art.server.specification.ServiceMethodSpecification.*;
import java.util.*;
import java.util.function.*;

public interface ServiceMethodModel {
    String getId();

    String getName();

    Function<ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> getDecorator();

    default ServiceMethodSpecificationBuilder implement(ServiceMethodSpecificationBuilder builder) {
        return getDecorator().apply(builder);
    }

}
