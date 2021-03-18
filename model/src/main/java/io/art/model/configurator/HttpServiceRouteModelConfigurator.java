package io.art.model.configurator;

import io.art.server.specification.ServiceMethodSpecification.*;
import lombok.*;

import java.util.*;
import java.util.function.*;

import static io.art.core.factory.MapFactory.*;
import static lombok.AccessLevel.PACKAGE;

@Getter(value = PACKAGE)
public class HttpServiceRouteModelConfigurator {
    private final Class<?> serviceClass;
    private final Map<String, HttpServiceMethodModelConfigurator> methods = map();
    private BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> decorator = (id, builder) -> builder;

    public HttpServiceRouteModelConfigurator(Class<?> serviceClass){
        this.serviceClass = serviceClass;
    }

    public HttpServiceRouteModelConfigurator method(String name) {
        return method(name, UnaryOperator.identity());
    }

    public HttpServiceRouteModelConfigurator method(String name, UnaryOperator<HttpServiceMethodModelConfigurator> configurator) {
        methods.putIfAbsent(name, configurator.apply(new HttpServiceMethodModelConfigurator(name)));
        return this;
    }

    public HttpServiceRouteModelConfigurator decorate(BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> decorator) {
        BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> current = this.decorator;
        this.decorator = (method, builder) -> {
            builder = current.apply(method, builder);
            return decorator.apply(method, builder);
        };
        return this;
    }

    protected String getId(){
        return serviceClass.getSimpleName();
    }

}
