package io.art.model.implementation.server;

import io.art.core.collection.*;
import static io.art.model.constants.ModelConstants.*;
import static io.art.server.specification.ServiceMethodSpecification.*;
import java.util.function.*;

public interface ServiceModel {
    Class<?> getServiceClass();

    String getId();

    ConfiguratorScope getScope();

    BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> getClassDecorator();

    ImmutableMap<String, RsocketServiceMethodModel> getMethods();

    ServiceMethodSpecificationBuilder implement(String id, ServiceMethodSpecificationBuilder current);
}
