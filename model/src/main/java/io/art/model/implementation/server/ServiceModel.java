package io.art.model.implementation.server;

import io.art.core.collection.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.model.constants.ModelConstants.*;
import static io.art.model.constants.ModelConstants.ConfiguratorScope.*;
import static io.art.server.specification.ServiceMethodSpecification.*;
import java.util.function.*;

public interface ServiceModel {
    Class<?> getServiceClass();

    String getId();

    ConfiguratorScope getScope();

    BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> getServiceDecorator();

    ImmutableMap<String, RsocketServiceMethodModel> getMethods();

    default ServiceMethodSpecificationBuilder implement(String id, ServiceMethodSpecificationBuilder current) {
        if (getScope() == CLASS) {
            return let(getServiceDecorator(), decorator -> decorator.apply(id, current));
        }
        return let(getMethods().get(id), methodModel -> methodModel.implement(current));
    }
}
