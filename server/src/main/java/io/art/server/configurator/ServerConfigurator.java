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
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
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
    private final Supplier<ServerConfiguration> configurationProvider;
    private final List<PackageBasedRegistration> packageBased = linkedList();
    private final List<ClassBasedRegistration> classBased = linkedList();
    private final List<MethodBasedRegistration> methodBased = linkedList();

    public ServerConfigurator forPackage(Supplier<MetaPackage> servicePackage) {
        return forPackage(servicePackage, identity());
    }

    public ServerConfigurator forPackage(Supplier<MetaPackage> servicePackage, UnaryOperator<ServiceMethodConfigurator> decorator) {
        packageBased.add(new PackageBasedRegistration(servicePackage, decorator));
        return this;
    }


    public ServerConfigurator forClass(Class<?> serviceClass) {
        return forClass(() -> declaration(serviceClass), identity());
    }

    public ServerConfigurator forClass(Class<?> serviceClass, UnaryOperator<ServiceMethodConfigurator> decorator) {
        return forClass(() -> declaration(serviceClass), decorator);
    }

    public ServerConfigurator forClass(Supplier<MetaClass<?>> serviceClass, UnaryOperator<ServiceMethodConfigurator> decorator) {
        classBased.add(new ClassBasedRegistration(serviceClass, decorator));
        return this;
    }


    public ServerConfigurator forMethod(Supplier<MetaClass<?>> serviceClass, Supplier<MetaMethod<?>> serviceMethod) {
        return forMethod(serviceClass, serviceMethod, identity());
    }

    public ServerConfigurator forMethod(Supplier<MetaClass<?>> serviceClass, Supplier<MetaMethod<?>> serviceMethod, UnaryOperator<ServiceMethodConfigurator> decorator) {
        methodBased.add(new MethodBasedRegistration(serviceClass, serviceMethod, decorator));
        return this;
    }


    protected LazyProperty<ImmutableArray<ServiceMethod>> get() {
        return lazy(this::createServiceMethods);
    }


    private ImmutableArray<ServiceMethod> createServiceMethods() {
        ImmutableArray.Builder<ServiceMethod> methods = immutableArrayBuilder();
        for (PackageBasedRegistration registration : packageBased) {
            registerPackages(methods, fixedArrayOf(registration.servicePackage.get()), registration.decorator);
        }

        for (ClassBasedRegistration registration : classBased) {
            registerMethods(methods, registration.serviceClass.get(), registration.decorator);
        }

        for (MethodBasedRegistration registration : methodBased) {
            methods.add(createServiceMethod(registration.serviceClass.get(), registration.serviceMethod.get(), registration.decorator));
        }

        return methods.build();
    }

    private void registerPackages(ImmutableArray.Builder<ServiceMethod> builder, Collection<MetaPackage> packages, UnaryOperator<ServiceMethodConfigurator> decorator) {
        for (MetaPackage metaPackage : packages) {
            registerPackages(builder, metaPackage.packages().values(), decorator);
            registerClasses(builder, metaPackage.classes().values(), decorator);
        }
    }

    private void registerClasses(ImmutableArray.Builder<ServiceMethod> builder, Collection<MetaClass<?>> classes, UnaryOperator<ServiceMethodConfigurator> decorator) {
        for (MetaClass<?> metaClass : classes) {
            registerClasses(builder, metaClass.classes().values(), decorator);
            registerMethods(builder, metaClass, decorator);
        }
    }

    private void registerMethods(ImmutableArray.Builder<ServiceMethod> builder, MetaClass<?> metaClass, UnaryOperator<ServiceMethodConfigurator> decorator) {
        for (MetaMethod<?> method : metaClass.methods()) {
            builder.add(createServiceMethod(metaClass, method, decorator));
        }
    }

    private ServiceMethod createServiceMethod(MetaClass<?> serviceClass, MetaMethod<?> serviceMethod, UnaryOperator<ServiceMethodConfigurator> decorator) {
        MetaType<?> inputType = orNull(() -> immutableArrayOf(serviceMethod.parameters().values()).get(0).type(), isNotEmpty(serviceMethod.parameters()));
        ServiceMethodIdentifier id = serviceMethodId(asId(serviceClass.definition().type()), serviceMethod.name());
        ServiceMethodBuilder builder = ServiceMethod.builder()
                .id(id)
                .outputType(serviceMethod.returnType())
                .invoker(new MetaMethodInvoker(serviceClass, serviceMethod));
        UnaryOperator<ServiceMethodBuilder> configurator = decorator.apply(new ServiceMethodConfigurator(id, configurationProvider.get())).configure();
        if (nonNull(inputType)) {
            return configurator.apply(builder.inputType(inputType)).build();
        }
        return configurator.apply(builder).build();
    }

    @RequiredArgsConstructor
    private static class ClassBasedRegistration {
        final Supplier<MetaClass<?>> serviceClass;
        final UnaryOperator<ServiceMethodConfigurator> decorator;
    }

    @RequiredArgsConstructor
    private static class PackageBasedRegistration {
        final Supplier<MetaPackage> servicePackage;
        final UnaryOperator<ServiceMethodConfigurator> decorator;
    }

    @RequiredArgsConstructor
    private static class MethodBasedRegistration {
        final Supplier<MetaClass<?>> serviceClass;
        final Supplier<MetaMethod<?>> serviceMethod;
        final UnaryOperator<ServiceMethodConfigurator> decorator;
    }
}
