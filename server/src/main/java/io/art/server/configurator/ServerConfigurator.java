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
    private final List<PackageBasedConfiguration> packageBased = linkedList();
    private final List<ClassBasedConfiguration> classBased = linkedList();
    private final List<MethodBasedConfiguration> methodBased = linkedList();

    public <T extends MetaLibrary> ServerConfigurator configurePackage(Function<T, MetaPackage> servicePackage) {
        return configurePackage(servicePackage, identity());
    }

    public <T extends MetaLibrary> ServerConfigurator configurePackage(Function<T, MetaPackage> servicePackage, UnaryOperator<ServiceMethodConfigurator> decorator) {
        packageBased.add(new PackageBasedConfiguration(() -> servicePackage.apply(library()), decorator));
        return this;
    }


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


    public <T extends MetaClass<?>> ServerConfigurator configureMethod(Class<?> serviceClass, Function<T, MetaMethod<?>> serviceMethod) {
        return configureMethod(serviceClass, serviceMethod, identity());
    }

    public <T extends MetaClass<?>> ServerConfigurator configureMethod(Class<?> serviceClass, Function<T, MetaMethod<?>> serviceMethod, UnaryOperator<ServiceMethodConfigurator> decorator) {
        return configureMethod(() -> cast(declaration(serviceClass)), serviceMethod, decorator);
    }

    public <T extends MetaClass<?>> ServerConfigurator configureMethod(Supplier<T> serviceClass, Function<T, MetaMethod<?>> serviceMethod) {
        return configureMethod(serviceClass, serviceMethod, identity());
    }

    public <T extends MetaClass<?>> ServerConfigurator configureMethod(Supplier<T> serviceClass, Function<T, MetaMethod<?>> serviceMethod, UnaryOperator<ServiceMethodConfigurator> decorator) {
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
        for (PackageBasedConfiguration configuration : packageBased) {
            MetaPackage servicePackage = configuration.servicePackage.get();
            registerClasses(methods, provider, new ClassesConfiguration(servicePackage.classes().values(), configuration.decorator));
            registerPackages(methods, provider, new PackagesConfiguration(servicePackage.packages().values(), configuration.decorator));
        }

        for (ClassBasedConfiguration configuration : classBased) {
            MetaClass<?> serviceClass = configuration.serviceClass.get();
            for (MetaMethod<?> method : serviceClass.methods()) {
                MethodConfiguration methodConfiguration = new MethodConfiguration(serviceClass, method, configuration.decorator);
                methods.add(createServiceMethod(provider, methodConfiguration));
            }
        }

        for (MethodBasedConfiguration configuration : methodBased) {
            MetaClass<?> serviceClass = configuration.serviceClass.get();
            MetaMethod<?> serviceMethod = configuration.serviceMethod.apply(cast(serviceClass));
            MethodConfiguration methodConfiguration = new MethodConfiguration(serviceClass, serviceMethod, configuration.decorator);
            methods.add(createServiceMethod(provider, methodConfiguration));
        }

        return immutableSetOf(setOf(methods));
    }

    private void registerPackages(List<ServiceMethod> builder, LazyProperty<ServerConfiguration> provider, PackagesConfiguration configuration) {
        UnaryOperator<ServiceMethodConfigurator> decorator = configuration.decorator;
        for (MetaPackage servicePackage : configuration.packages) {
            registerPackages(builder, provider, new PackagesConfiguration(servicePackage.packages().values(), decorator));
            registerClasses(builder, provider, new ClassesConfiguration(servicePackage.classes().values(), decorator));
        }
    }

    private void registerClasses(List<ServiceMethod> builder, LazyProperty<ServerConfiguration> provider, ClassesConfiguration configuration) {
        UnaryOperator<ServiceMethodConfigurator> decorator = configuration.decorator;
        for (MetaClass<?> serviceClass : configuration.classes) {
            registerClasses(builder, provider, new ClassesConfiguration(serviceClass.classes().values(), decorator));
            for (MetaMethod<?> method : serviceClass.methods()) {
                builder.add(createServiceMethod(provider, new MethodConfiguration(serviceClass, method, decorator)));
            }
        }
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
        builder = decorator.apply(new ServiceMethodConfigurator(id, provider.get())).configure(builder, inputType);
        return nonNull(inputType) ? builder.inputType(inputType).build() : builder.build();
    }


    @RequiredArgsConstructor
    private static class ClassBasedConfiguration {
        final Supplier<MetaClass<?>> serviceClass;
        final UnaryOperator<ServiceMethodConfigurator> decorator;
    }

    @RequiredArgsConstructor
    private static class PackageBasedConfiguration {
        final Supplier<MetaPackage> servicePackage;
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

    @RequiredArgsConstructor
    private static class ClassesConfiguration {
        final Collection<MetaClass<?>> classes;
        final UnaryOperator<ServiceMethodConfigurator> decorator;
    }

    @RequiredArgsConstructor
    private static class PackagesConfiguration {
        final Collection<MetaPackage> packages;
        final UnaryOperator<ServiceMethodConfigurator> decorator;
    }
}
