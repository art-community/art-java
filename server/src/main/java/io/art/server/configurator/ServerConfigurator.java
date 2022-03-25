package io.art.server.configurator;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import static io.art.meta.Meta.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

@Public
public interface ServerConfigurator {
    default ServerConfigurator service(Class<?> serviceClass) {
        return service(serviceClass, identity());
    }

    default ServerConfigurator service(Class<?> serviceClass, UnaryOperator<ServiceMethodConfigurator> decorator) {
        return service(() -> declaration(serviceClass), decorator);
    }

    default <M extends MetaClass<?>> ServerConfigurator service(Supplier<M> serviceClass) {
        return service(serviceClass, identity());
    }

    <M extends MetaClass<?>> ServerConfigurator service(Supplier<M> serviceClass, UnaryOperator<ServiceMethodConfigurator> decorator);

    default <M extends MetaClass<?>> ServerConfigurator method(Supplier<MetaMethod<M, ?>> serviceMethod) {
        return method(serviceMethod, identity());
    }

    <M extends MetaClass<?>> ServerConfigurator method(Supplier<MetaMethod<M, ?>> serviceMethod, UnaryOperator<ServiceMethodConfigurator> decorator);
}
