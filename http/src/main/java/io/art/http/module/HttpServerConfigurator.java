package io.art.http.module;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.http.configuration.*;
import io.art.http.configuration.HttpRouteConfiguration.*;
import io.art.http.configuration.HttpServerConfiguration.*;
import io.art.http.constants.HttpModuleConstants.*;
import io.art.meta.model.*;
import io.art.server.configuration.*;
import io.art.server.configurator.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.http.constants.HttpModuleConstants.HttpRouteType.*;
import static io.art.http.strategy.RouteByServiceMethodStrategy.*;
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

    public HttpServerConfigurator fromService(Class<?> serviceClass) {
        return configureService(serviceClass, UnaryOperator.identity());
    }

    public HttpServerConfigurator fromService(Class<?> serviceClass, UnaryOperator<HttpRouteConfigurationBuilder> decorator) {
        classBased.add(new ClassBasedConfiguration(() -> declaration(serviceClass), decorator));
        return this;
    }

    public <T extends MetaClass<?>>
    HttpServerConfigurator fromMethod(Class<?> serviceClass, Function<T, MetaMethod<?>> serviceMethod) {
        return fromMethod(serviceClass, serviceMethod, identity());
    }

    public <T extends MetaClass<?>>
    HttpServerConfigurator fromMethod(Class<?> serviceClass, Function<T, MetaMethod<?>> serviceMethod, UnaryOperator<HttpRouteConfigurationBuilder> decorator) {
        methodBased.add(new MethodBasedConfiguration(() -> declaration(serviceClass), serviceMethod, decorator));
        return cast(this);
    }

    HttpServerConfiguration configure(HttpServerConfiguration current) {
        return configurator.apply(current.toBuilder().routes(lazy(this::configureRoutes))).build();
    }

    ServerConfiguration configureServer(LazyProperty<ServerConfiguration> configurationProvider, ServerConfiguration current) {
        return configure(configurationProvider, current);
    }

    private ImmutableArray<HttpRouteConfiguration> configureRoutes() {
        ImmutableArray.Builder<HttpRouteConfiguration> routes = immutableArrayBuilder();
        for (ClassBasedConfiguration classBasedConfiguration : classBased) {
            HttpRouteConfigurationBuilder configurationBuilder = HttpRouteConfiguration.builder();
            MetaClass<?> metaClass = classBasedConfiguration.serviceClass.get();
            for (Map.Entry<HttpRouteType, MetaMethod<?>> method : extractHttpMethods(metaClass).entrySet()) {
                classBasedConfiguration.decorator.apply(configurationBuilder.type(method.getKey())
                        .path(byServiceMethod())
                        .serviceMethodId(serviceMethodId(asId(metaClass.getClass()), method.getValue().name())));
                routes.add(configurationBuilder.build());
            }
        }
        for (MethodBasedConfiguration methodBasedConfiguration : methodBased) {
            HttpRouteConfigurationBuilder configurationBuilder = HttpRouteConfiguration.builder();
            MetaClass<?> metaClass = methodBasedConfiguration.serviceClass.get();
            MetaMethod<?> method = methodBasedConfiguration.serviceMethod.apply(cast(metaClass));
            if (methodStartWithExcludePath(method.name())) {
                methodBasedConfiguration.decorator.apply(configurationBuilder.type(extractRouteType(method.name()))
                        .path(byServiceMethod())
                        .serviceMethodId(serviceMethodId(asId(metaClass.getClass()), method.name())));
                routes.add(configurationBuilder.build());
            }
        }
        return routes.build();
    }

    private static Map<HttpRouteType, MetaMethod<?>> extractHttpMethods(MetaClass<?> serviceClass) {
        return serviceClass.methods()
                .stream()
                .filter(method -> method.parameters().size() < 2)
                .filter(method -> methodStartWithExcludePath(method.name()))
                .collect(mapCollector(method -> extractRouteType(method.name()), Function.identity()));
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

    @RequiredArgsConstructor
    private static class MethodConfiguration {
        final MetaClass<?> serviceClass;
        final MetaMethod<?> serviceMethod;
        final UnaryOperator<HttpRouteConfigurationBuilder> decorator;
    }
}
