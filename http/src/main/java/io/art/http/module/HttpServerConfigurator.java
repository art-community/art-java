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
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

@Public
public class HttpServerConfigurator extends ServerConfigurator<HttpServerConfigurator> {
    private UnaryOperator<HttpServerConfigurationBuilder> configurator = identity();
    private final List<ClassBasedConfiguration> classBased = linkedList();
    private final List<MethodBasedConfiguration> methodBased = linkedList();
    private final List<UnaryOperator<HttpRouteConfigurationBuilder>> pathRoutes = linkedList();

    public HttpServerConfigurator configure(UnaryOperator<HttpServerConfigurationBuilder> configurator) {
        this.configurator = configurator;
        return this;
    }

    public HttpServerConfigurator routes(Class<?> serviceClass) {
        return routes(serviceClass, UnaryOperator.identity());
    }

    public HttpServerConfigurator routes(Class<?> serviceClass, UnaryOperator<HttpRouteConfigurationBuilder> decorator) {
        service(serviceClass);
        classBased.add(new ClassBasedConfiguration(() -> declaration(serviceClass), decorator));
        return this;
    }

    public HttpServerConfigurator route(MetaMethod<?> serviceMethod) {
        return route(serviceMethod, identity());
    }

    public HttpServerConfigurator route(MetaMethod<?> serviceMethod, UnaryOperator<HttpRouteConfigurationBuilder> decorator) {
        method(serviceMethod);
        methodBased.add(new MethodBasedConfiguration(serviceMethod::owner, serviceMethod, decorator));
        return this;
    }

    public HttpServerConfigurator file(String uri, Path path) {
        return file(uri, path, UnaryOperator.identity());
    }

    public HttpServerConfigurator file(String uri, Path path, UnaryOperator<HttpRouteConfigurationBuilder> decorator) {
        pathRoutes.add(builder -> decorator.apply(builder
                .type(PATH)
                .uri(manual(uri))
                .pathConfiguration(HttpPathRouteConfiguration.builder()
                        .path(path)
                        .build())));
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
                configurationBuilder.type(extractRouteType(method))
                        .uri(byServiceMethod())
                        .serviceMethodId(serviceMethodId(idByDash(metaClass.definition().type()), method.name()));
                getServiceDecorator(metaClass).apply(configurationBuilder);
                getMethodDecorator(metaClass, method).apply(configurationBuilder);
                routes.add(configurationBuilder.build());
            }
        }
        for (MethodBasedConfiguration methodBasedConfiguration : methodBased) {
            HttpRouteConfigurationBuilder configurationBuilder = routeConfiguration().toBuilder();
            MetaClass<?> metaClass = methodBasedConfiguration.serviceClass.get();
            MetaMethod<?> method = methodBasedConfiguration.serviceMethod;
            if (!method.isKnown()) continue;
            configurationBuilder
                    .type(extractRouteType(method))
                    .uri(byServiceMethod())
                    .serviceMethodId(serviceMethodId(idByDash(metaClass.definition().type()), method.name()));
            getMethodDecorator(metaClass, method).apply(configurationBuilder);
            getServiceDecorator(metaClass).apply(configurationBuilder);
            routes.add(configurationBuilder.build());
        }
        for (UnaryOperator<HttpRouteConfigurationBuilder> pathRoute : pathRoutes) {
            routes.add(pathRoute.apply(routeConfiguration().toBuilder()).build());
        }
        return routes.build();
    }

    private static Set<MetaMethod<?>> extractHttpMethods(MetaClass<?> serviceClass) {
        return serviceClass.methods()
                .stream()
                .filter(method -> method.parameters().size() < 2)
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
                .filter(methodConfiguration -> method.equals(methodConfiguration.serviceMethod))
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
        final MetaMethod<?> serviceMethod;
        final UnaryOperator<HttpRouteConfigurationBuilder> decorator;
    }
}
