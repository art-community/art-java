package io.art.model.configurator;

import io.art.model.implementation.server.*;
import io.art.server.specification.ServiceMethodSpecification.*;
import lombok.*;

import java.util.*;
import java.util.function.*;

import static io.art.http.constants.HttpModuleConstants.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.factory.MapFactory.*;
import static lombok.AccessLevel.*;

@Getter(value = PACKAGE)
public class HttpServiceModelConfigurator {
    private final Class<?> serviceClass;
    private final Map<String, HttpServiceMethodModelConfigurator> methods = map();
    private final HttpServiceExceptionMappingConfigurator exceptionMapping;
    private BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> decorator = (id, builder) -> builder;
    private boolean logging;

    public HttpServiceModelConfigurator(Class<?> serviceClass){
        this.serviceClass = serviceClass;
        this.exceptionMapping = new HttpServiceExceptionMappingConfigurator(serviceClass);
    }

    private HttpServiceModelConfigurator method(String methodName, UnaryOperator<HttpServiceMethodModelConfigurator> configurator) {
        HttpServiceMethodModelConfigurator newMethod = new HttpServiceMethodModelConfigurator(methodName).logging(logging);
        methods.putIfAbsent(methodName, configurator.apply(newMethod));
        return this;
    }

    public HttpServiceModelConfigurator get(String methodName){
        return method(methodName, method -> method.httpMethod(HttpMethodType.GET));
    }

    public HttpServiceModelConfigurator post(String methodName){
        return method(methodName, method -> method.httpMethod(HttpMethodType.POST));
    }

    public HttpServiceModelConfigurator put(String methodName){
        return method(methodName, method -> method.httpMethod(HttpMethodType.PUT));
    }

    public HttpServiceModelConfigurator delete(String methodName){
        return method(methodName, method -> method.httpMethod(HttpMethodType.DELETE));
    }

    public HttpServiceModelConfigurator options(String methodName){
        return method(methodName, method -> method.httpMethod(HttpMethodType.OPTIONS));
    }

    public HttpServiceModelConfigurator head(String methodName){
        return method(methodName, method -> method.httpMethod(HttpMethodType.HEAD));
    }

    public HttpServiceModelConfigurator websocket(String methodName){
        return method(methodName, method -> method.httpMethod(HttpMethodType.WEBSOCKET));
    }

    public HttpServiceModelConfigurator get(String methodName, UnaryOperator<HttpServiceMethodModelConfigurator> configurator){
        return method(methodName, method -> configurator.apply(method.httpMethod(HttpMethodType.GET)));
    }

    public HttpServiceModelConfigurator post(String methodName, UnaryOperator<HttpServiceMethodModelConfigurator> configurator){
        return method(methodName, method -> configurator.apply(method.httpMethod(HttpMethodType.POST)));
    }

    public HttpServiceModelConfigurator put(String methodName, UnaryOperator<HttpServiceMethodModelConfigurator> configurator){
        return method(methodName, method -> configurator.apply(method.httpMethod(HttpMethodType.PUT)));
    }

    public HttpServiceModelConfigurator delete(String methodName, UnaryOperator<HttpServiceMethodModelConfigurator> configurator){
        return method(methodName, method -> configurator.apply(method.httpMethod(HttpMethodType.DELETE)));
    }

    public HttpServiceModelConfigurator options(String methodName, UnaryOperator<HttpServiceMethodModelConfigurator> configurator){
        return method(methodName, method -> configurator.apply(method.httpMethod(HttpMethodType.OPTIONS)));
    }

    public HttpServiceModelConfigurator head(String methodName, UnaryOperator<HttpServiceMethodModelConfigurator> configurator){
        return method(methodName, method -> configurator.apply(method.httpMethod(HttpMethodType.HEAD)));
    }

    public HttpServiceModelConfigurator websocket(String methodName, UnaryOperator<HttpServiceMethodModelConfigurator> configurator){
        return method(methodName, method -> configurator.apply(method.httpMethod(HttpMethodType.WEBSOCKET)));
    }



    public HttpServiceModelConfigurator exceptions(UnaryOperator<HttpServiceExceptionMappingConfigurator> configurator){
        configurator.apply(exceptionMapping);
        return this;
    }



    public HttpServiceModelConfigurator decorate(BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> decorator) {
        BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> current = this.decorator;
        this.decorator = (method, builder) -> {
            builder = current.apply(method, builder);
            return decorator.apply(method, builder);
        };
        return this;
    }

    public HttpServiceModelConfigurator logging(boolean isLogging) {
        this.logging = isLogging;
        return this;
    }

    protected String getId(){
        return serviceClass.getSimpleName();
    }

    protected HttpServiceModel configure(String path){
        return HttpServiceModel.builder()
                .id(getId())
                .path(path)
                .serviceClass(serviceClass)
                .methods(methods
                        .entrySet()
                        .stream()
                        .collect(immutableMapCollector(entry -> entry.getValue().getId(), entry -> entry.getValue().configure())))
                .decorator(decorator)
                .exceptionsMapper(exceptionMapping.configure())
                .build();
    }
}
