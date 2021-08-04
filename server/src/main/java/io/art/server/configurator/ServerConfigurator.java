package io.art.server.configurator;

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.meta.invoker.*;
import io.art.meta.model.*;
import io.art.server.configuration.*;
import io.art.server.method.*;
import io.art.server.method.ServiceMethod.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.extensions.FunctionExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.module.MetaModule.*;
import static java.util.Objects.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public abstract class ServerConfigurator {
    private final List<ClassBasedConfiguration> classBased = linkedList();
    private final List<MethodBasedConfiguration> methodBased = linkedList();


    public ServerConfigurator configureClass(Class<?> serviceClass) {
        return configureClass(() -> declaration(serviceClass), identity());
    }

    public ServerConfigurator configureClass(Class<?> serviceClass, UnaryOperator<ServiceMethodConfigurator> decorator) {
        return configureClass(() -> declaration(serviceClass), decorator);
    }

    public ServerConfigurator configureClass(Supplier<MetaClass<?>> serviceClass) {
        return configureClass(serviceClass, identity());
    }

    public ServerConfigurator configureClass(Supplier<MetaClass<?>> serviceClass, UnaryOperator<ServiceMethodConfigurator> decorator) {
        classBased.add(new ClassBasedConfiguration(serviceClass, decorator));
        return this;
    }


    public <T extends MetaClass<?>>
    ServerConfigurator configureMethod(Class<?> serviceClass, Function<T, MetaMethod<?>> serviceMethod) {
        return configureMethod(serviceClass, serviceMethod, identity());
    }

    public <T extends MetaClass<?>>
    ServerConfigurator configureMethod(Class<?> serviceClass, Function<T, MetaMethod<?>> serviceMethod, UnaryOperator<ServiceMethodConfigurator> decorator) {
        return configureMethod(() -> cast(declaration(serviceClass)), serviceMethod, decorator);
    }

    public <T extends MetaClass<?>>
    ServerConfigurator configureMethod(Supplier<T> serviceClass, Function<T, MetaMethod<?>> serviceMethod) {
        return configureMethod(serviceClass, serviceMethod, identity());
    }

    public <T extends MetaClass<?>>
    ServerConfigurator configureMethod(Supplier<T> serviceClass, Function<T, MetaMethod<?>> serviceMethod, UnaryOperator<ServiceMethodConfigurator> decorator) {
        methodBased.add(new MethodBasedConfiguration(serviceClass, serviceMethod, decorator));
        return this;
    }


    protected ServerConfiguration configure(LazyProperty<ServerConfiguration> provider, ServerConfiguration current) {
        return current.toBuilder()
                .serviceMethods(lazy(() -> createMethods(provider)))
                .build();
    }


    private ImmutableSet<ServiceMethod> createMethods(LazyProperty<ServerConfiguration> provider) {
        List<ServiceMethod> methods = linkedList();

        for (ClassBasedConfiguration configuration : classBased) {
            MetaClass<?> serviceClass = configuration.serviceClass.get();
            for (MetaMethod<?> method : serviceClass.methods()) {
                UnaryOperator<ServiceMethodConfigurator> decorator = methodBased
                        .stream()
                        .filter(methodConfiguration -> serviceClass.equals(methodConfiguration.serviceClass.get()))
                        .filter(methodConfiguration -> method.equals(methodConfiguration.serviceMethod))
                        .findFirst()
                        .map(methodConfiguration -> methodConfiguration.decorator)
                        .orElse(identity());
                decorator = then(configuration.decorator, decorator);
                MethodConfiguration methodConfiguration = new MethodConfiguration(serviceClass, method, decorator);
                methods.add(createServiceMethod(provider, methodConfiguration));
            }
        }

        for (MethodBasedConfiguration configuration : methodBased) {
            MetaClass<?> serviceClass = configuration.serviceClass.get();
            MetaMethod<?> serviceMethod = configuration.serviceMethod.apply(cast(serviceClass));
            UnaryOperator<ServiceMethodConfigurator> decorator = classBased
                    .stream()
                    .filter(methodConfiguration -> serviceClass.equals(methodConfiguration.serviceClass.get()))
                    .findFirst()
                    .map(methodConfiguration -> methodConfiguration.decorator)
                    .orElse(identity());
            decorator = then(configuration.decorator, decorator);
            MethodConfiguration methodConfiguration = new MethodConfiguration(serviceClass, serviceMethod, decorator);
            methods.add(createServiceMethod(provider, methodConfiguration));
        }

        return immutableSetOf(setOf(methods));
    }

    private ServiceMethod createServiceMethod(LazyProperty<ServerConfiguration> provider, MethodConfiguration methodConfiguration) {
        MetaMethod<?> serviceMethod = methodConfiguration.serviceMethod;
        MetaClass<?> serviceClass = methodConfiguration.serviceClass;
        UnaryOperator<ServiceMethodConfigurator> decorator = methodConfiguration.decorator;
        MetaType<?> inputType = orNull(() -> immutableArrayOf(serviceMethod.parameters().values()).get(0).type(), isNotEmpty(serviceMethod.parameters()));
        ServiceMethodIdentifier id = serviceMethodId(asId(serviceClass.definition().type()), serviceMethod.name());
        ServiceMethodBuilder builder = ServiceMethod.builder()
                .id(id)
                .outputType(serviceMethod.returnType())
                .invoker(new MetaMethodInvoker(serviceClass, serviceMethod));
        ServiceMethodConfigurator configurator = new ServiceMethodConfigurator(id, provider.get());
        builder = decorator.apply(configurator).configure(builder, inputType);
        return nonNull(inputType) ? builder.inputType(inputType).build() : builder.build();
    }


    @RequiredArgsConstructor
    private static class ClassBasedConfiguration {
        final Supplier<MetaClass<?>> serviceClass;
        final UnaryOperator<ServiceMethodConfigurator> decorator;
    }

    @RequiredArgsConstructor
    private static class MethodBasedConfiguration {
        final Supplier<? extends MetaClass<?>> serviceClass;
        final Function<? extends MetaClass<?>, MetaMethod<?>> serviceMethod;
        final UnaryOperator<ServiceMethodConfigurator> decorator;
    }

    @RequiredArgsConstructor
    private static class MethodConfiguration {
        final MetaClass<?> serviceClass;
        final MetaMethod<?> serviceMethod;
        final UnaryOperator<ServiceMethodConfigurator> decorator;
    }
}
