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
    private boolean logging;

    public HttpServiceRouteModelConfigurator(Class<?> serviceClass){
        this.serviceClass = serviceClass;
    }

    public HttpServiceRouteModelConfigurator method(String methodName) {
        return method(methodName, UnaryOperator.identity());
    }

    public HttpServiceRouteModelConfigurator method(String methodName, UnaryOperator<HttpServiceMethodModelConfigurator> configurator) {
        HttpServiceMethodModelConfigurator newMethod = new HttpServiceMethodModelConfigurator(methodName).logging(logging);
        methods.putIfAbsent(methodName, configurator.apply(newMethod));
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

    public HttpServiceRouteModelConfigurator logging(boolean isLogging) {
        this.logging = isLogging;
        return this;
    }

    protected String getId(){
        return serviceClass.getSimpleName();
    }

}
