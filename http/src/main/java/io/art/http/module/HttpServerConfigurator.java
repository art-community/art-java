package io.art.http.module;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.extensions.*;
import io.art.core.property.*;
import io.art.http.configuration.*;
import io.art.http.configuration.HttpServerConfiguration.*;
import io.art.meta.model.*;
import io.art.server.configuration.*;
import io.art.server.configurator.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.collector.SetCollector.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.http.configuration.HttpRouteConfiguration.*;
import static io.art.http.constants.HttpModuleConstants.HttpRouteType.*;
import static io.art.http.path.HttpServingUri.*;
import static io.art.meta.Meta.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
public class HttpServerConfigurator extends ServerConfigurator<HttpServerConfigurator> {
    private UnaryOperator<HttpServerConfigurationBuilder> configurator = identity();
    private final List<ClassBasedConfiguration> classBased = linkedList();
    private final List<MethodBasedConfiguration> methodBased = linkedList();

    public HttpServerConfigurator configure(UnaryOperator<HttpServerConfigurationBuilder> configurator) {
        this.configurator = configurator;
        return this;
    }

    public HttpServerConfigurator route(Class<?> serviceClass) {
        return route(serviceClass, UnaryOperator.identity());
    }

    public HttpServerConfigurator route(Class<?> serviceClass, UnaryOperator<HttpRouteConfigurationBuilder> decorator) {
        service(serviceClass);
        classBased.add(new ClassBasedConfiguration(() -> declaration(serviceClass), decorator));
        return this;
    }

    public <T extends MetaClass<?>>
    HttpServerConfigurator route(Class<?> serviceClass, Function<T, MetaMethod<?>> serviceMethod) {
        return route(serviceClass, serviceMethod, identity());
    }

    public <T extends MetaClass<?>>
    HttpServerConfigurator route(Class<?> serviceClass, Function<T, MetaMethod<?>> serviceMethod, UnaryOperator<HttpRouteConfigurationBuilder> decorator) {
        method(serviceClass, serviceMethod);
        methodBased.add(new MethodBasedConfiguration(() -> declaration(serviceClass), serviceMethod, decorator));
        return this;
    }

    HttpServerConfiguration configure(HttpServerConfiguration current) {
        return configurator
                .apply(current.toBuilder().routes(lazy(this::configureRoutes)))
                .build();
    }

    ServerConfiguration configureServer(LazyProperty<ServerConfiguration> configurationProvider, ServerConfiguration current) {
        return configure(configurationProvider, current);
    }

    private ImmutableArray<HttpRouteConfiguration> configureRoutes() {
        ImmutableArray.Builder<HttpRouteConfiguration> routes = immutableArrayBuilder();
        for (ClassBasedConfiguration classBasedConfiguration : classBased) {
            HttpRouteConfigurationBuilder configurationBuilder = routeConfiguration().toBuilder();
            MetaClass<?> metaClass = classBasedConfiguration.serviceClass.get();
            for (MetaMethod<?> method : extractHttpMethods(metaClass)) {
                if (!method.isKnown()) continue;
                configurationBuilder.type(extractRouteType(method.name()))
                        .uri(byServiceMethod())
                        .serviceMethodId(serviceMethodId(asId(metaClass.definition().type()), method.name()));
                getServiceDecorator(metaClass).apply(configurationBuilder);
                getMethodDecorator(metaClass, method).apply(configurationBuilder);
                routes.add(configurationBuilder.build());
            }
        }
        for (MethodBasedConfiguration methodBasedConfiguration : methodBased) {
            HttpRouteConfigurationBuilder configurationBuilder = routeConfiguration().toBuilder();
            MetaClass<?> metaClass = methodBasedConfiguration.serviceClass.get();
            MetaMethod<?> method = methodBasedConfiguration.serviceMethod.apply(cast(metaClass));
            if (!method.isKnown()) continue;
            if (methodHasRouteTypePrefix(method.name())) configurationBuilder.type(extractRouteType(method.name()));
            configurationBuilder
                    .uri(byServiceMethod())
                    .serviceMethodId(serviceMethodId(asId(metaClass.definition().type()), method.name()));
            getMethodDecorator(metaClass, method).apply(configurationBuilder);
            getServiceDecorator(metaClass).apply(configurationBuilder);
            routes.add(configurationBuilder.build());
        }
        return routes.build();
    }

    private static Set<MetaMethod<?>> extractHttpMethods(MetaClass<?> serviceClass) {
        return serviceClass.methods()
                .stream()
                .filter(method -> method.parameters().size() < 2)
                .filter(method -> methodHasRouteTypePrefix(method.name()))
                .collect(setCollector());
    }

    private UnaryOperator<HttpRouteConfigurationBuilder> getServiceDecorator(MetaClass<?> serviceClass) {
        return classBased
                .stream()
                .filter(classConfiguration -> serviceClass.equals(classConfiguration.serviceClass.get()))
                .map(classConfiguration -> classConfiguration.decorator)
                .reduce(FunctionExtensions::then)
                .orElse(identity());
    }

    private UnaryOperator<HttpRouteConfigurationBuilder> getMethodDecorator(MetaClass<?> serviceClass, MetaMethod<?> method) {
        return methodBased
                .stream()
                .filter(methodConfiguration -> serviceClass.equals(methodConfiguration.serviceClass.get()))
                .filter(methodConfiguration -> method.equals(methodConfiguration.serviceMethod.apply(cast(methodConfiguration.serviceClass.get()))))
                .map(methodConfiguration -> methodConfiguration.decorator)
                .reduce(FunctionExtensions::then)
                .orElse(identity());
    }

    @RequiredArgsConstructor
    private static class ClassBasedConfiguration {
        final Supplier<MetaClass<?>> serviceClass;
        final UnaryOperator<HttpRouteConfigurationBuilder> decorator;
    }

    @RequiredArgsConstructor
    private static class MethodBasedConfiguration {
        final Supplier<? extends MetaClass<?>> serviceClass;
        final Function<? extends MetaClass<?>, MetaMethod<?>> serviceMethod;
        final UnaryOperator<HttpRouteConfigurationBuilder> decorator;
    }
}
