package io.art.http.module;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.extensions.*;
import io.art.http.configuration.*;
import io.art.http.configuration.HttpServerConfiguration.*;
import io.art.meta.model.*;
import io.art.server.configuration.*;
import io.art.server.configurator.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.SetCollector.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.http.configuration.HttpRouteConfiguration.*;
import static io.art.http.constants.HttpModuleConstants.HttpRouteType.*;
import static io.art.http.module.HttpModule.*;
import static io.art.http.path.HttpServingUri.*;
import static io.art.meta.Meta.*;
import static java.util.function.UnaryOperator.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

@Public
public class HttpServerConfigurator {
    private UnaryOperator<HttpServerConfigurationBuilder> configurator = identity();
    private final List<ClassBasedConfiguration> classBased = linkedList();
    private final List<MethodBasedConfiguration> methodBased = linkedList();
    private final List<UnaryOperator<HttpRouteConfigurationBuilder>> pathRoutes = linkedList();
    private final ServerConfiguratorImplementation delegate = new ServerConfiguratorImplementation();

    public HttpServerConfigurator routes(Class<?> serviceClass) {
        return routes(serviceClass, UnaryOperator.identity());
    }

    public HttpServerConfigurator routes(Class<?> serviceClass, UnaryOperator<HttpRouteConfigurationBuilder> decorator) {
        delegate.service(serviceClass);
        classBased.add(new ClassBasedConfiguration(() -> declaration(serviceClass), decorator));
        return this;
    }

    public <M extends MetaClass<?>> HttpServerConfigurator routes(Supplier<M> serviceClass) {
        return routes(serviceClass, UnaryOperator.identity());
    }

    public <M extends MetaClass<?>> HttpServerConfigurator routes(Supplier<M> serviceClass, UnaryOperator<HttpRouteConfigurationBuilder> decorator) {
        delegate.service(serviceClass);
        classBased.add(new ClassBasedConfiguration(serviceClass, decorator));
        return this;
    }

    public <M extends MetaClass<?>> HttpServerConfigurator route(Supplier<MetaMethod<M, ?>> serviceMethod) {
        return route(serviceMethod, identity());
    }

    public <M extends MetaClass<?>> HttpServerConfigurator route(Supplier<MetaMethod<M, ?>> serviceMethod, UnaryOperator<HttpRouteConfigurationBuilder> decorator) {
        delegate.method(serviceMethod);
        methodBased.add(new MethodBasedConfiguration(() -> serviceMethod.get().owner(), cast(serviceMethod), decorator));
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

    public HttpServerConfigurator setup(UnaryOperator<HttpServerConfigurationBuilder> configurator) {
        this.configurator = configurator;
        return this;
    }

    public HttpServerConfigurator configure(UnaryOperator<ServerConfigurator> configurator) {
        configurator.apply(delegate);
        return this;
    }

    HttpServerConfiguration createHttpConfiguration(HttpServerConfiguration current) {
        return configurator
                .apply(current.toBuilder().routes(lazy(this::configureRoutes)))
                .build();
    }

    ServerConfiguration createServerConfiguration(ServerConfiguration current) {
        return delegate.createConfiguration(lazy(() -> httpModule().configuration().getServer()), current);
    }

    private ImmutableSet<HttpRouteConfiguration> configureRoutes() {
        ImmutableSet.Builder<HttpRouteConfiguration> routes = ImmutableSet.immutableSetBuilder();
        for (ClassBasedConfiguration classBasedConfiguration : classBased) {
            HttpRouteConfigurationBuilder configurationBuilder = routeConfiguration().toBuilder();
            MetaClass<?> metaClass = classBasedConfiguration.serviceClass.get();
            for (MetaMethod<MetaClass<?>, ?> method : extractHttpMethods(metaClass)) {
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
            MetaMethod<MetaClass<?>, ?> method = cast(methodBasedConfiguration.serviceMethod.get());
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

    private static Set<MetaMethod<MetaClass<?>, ?>> extractHttpMethods(MetaClass<?> serviceClass) {
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

    private UnaryOperator<HttpRouteConfigurationBuilder> getMethodDecorator(MetaClass<?> serviceClass, MetaMethod<MetaClass<?>, ?> method) {
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
        final Supplier<? extends MetaClass<?>> serviceClass;
        final UnaryOperator<HttpRouteConfigurationBuilder> decorator;
    }

    @RequiredArgsConstructor
    private static class MethodBasedConfiguration {
        final Supplier<? extends MetaClass<?>> serviceClass;
        final Supplier<MetaMethod<? extends MetaClass<?>, ?>> serviceMethod;
        final UnaryOperator<HttpRouteConfigurationBuilder> decorator;
    }
}
