package io.art.model.implementation.server;

import io.art.core.collection.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.server.specification.ServiceMethodSpecification.*;
import java.util.*;
import java.util.function.*;

public interface ServiceModel {
    String getId();

    Class<?> getServiceClass();

    BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> getDecorator();

    ImmutableMap<String, ServiceMethodModel> getMethods();

    default Optional<ServiceMethodModel> getMethodByName(String name) {
        return getMethods()
                .values()
                .stream()
                .filter(action -> action.getName().equals(name))
                .findFirst();
    }

    default ServiceMethodSpecificationBuilder implement(String id, ServiceMethodSpecificationBuilder current) {
        ImmutableMap<String, ServiceMethodModel> methods = getMethods();
        if (isEmpty(methods)) {
            return let(getDecorator(), decorator -> decorator.apply(id, current));
        }
        return let(methods.get(id), methodModel -> methodModel.implement(current));
    }
}
