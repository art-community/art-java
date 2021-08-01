package io.art.communicator.configurator;

import io.art.communicator.*;
import io.art.communicator.action.*;
import io.art.communicator.action.CommunicatorAction.*;
import io.art.communicator.configuration.*;
import io.art.communicator.proxy.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import lombok.*;
import static io.art.communicator.factory.CommunicatorProxyFactory.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.model.CommunicatorActionIdentifier.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.module.MetaModule.*;
import static java.util.Objects.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public abstract class CommunicatorConfigurator {
    private final Supplier<CommunicatorConfiguration> configurationProvider;
    private final Supplier<? extends Communication> communication;
    private final List<PackageBasedRegistration> packageBased = linkedList();
    private final List<ClassBasedRegistration> classBased = linkedList();

    public CommunicatorConfigurator forPackage(Supplier<MetaPackage> proxyPackage) {
        return forPackage(proxyPackage, identity());
    }

    public CommunicatorConfigurator forPackage(Supplier<MetaPackage> proxyPackage, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        packageBased.add(new PackageBasedRegistration(proxyPackage, decorator));
        return this;
    }


    public CommunicatorConfigurator forClass(Class<?> proxyClass) {
        return forClass(() -> declaration(proxyClass), identity());
    }

    public CommunicatorConfigurator forClass(Class<?> proxyClass, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        return forClass(() -> declaration(proxyClass), decorator);
    }

    public CommunicatorConfigurator forClass(Supplier<MetaClass<?>> proxyClass, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        classBased.add(new ClassBasedRegistration(proxyClass, decorator));
        return this;
    }


    protected LazyProperty<ImmutableMap<Class<?>, CommunicatorProxy<?>>> get() {
        return lazy(this::createProxies);
    }


    private ImmutableMap<Class<?>, CommunicatorProxy<?>> createProxies() {
        ImmutableMap.Builder<Class<?>, CommunicatorProxy<?>> proxies = immutableMapBuilder();
        for (PackageBasedRegistration registration : packageBased) {
            registerPackages(proxies, fixedArrayOf(registration.proxyPackage.get()), registration.decorator);
        }

        for (ClassBasedRegistration registration : classBased) {
            registerMethods(proxies, registration.proxyClass.get(), registration.decorator);
        }

        return proxies.build();
    }

    private void registerPackages(ImmutableMap.Builder<Class<?>, CommunicatorProxy<?>> builder,
                                  Collection<MetaPackage> packages,
                                  UnaryOperator<CommunicatorActionConfigurator> decorator) {
        for (MetaPackage metaPackage : packages) {
            registerPackages(builder, metaPackage.packages().values(), decorator);
            registerClasses(builder, metaPackage.classes().values(), decorator);
        }
    }

    private void registerClasses(ImmutableMap.Builder<Class<?>, CommunicatorProxy<?>> builder,
                                 Collection<MetaClass<?>> classes,
                                 UnaryOperator<CommunicatorActionConfigurator> decorator) {
        for (MetaClass<?> metaClass : classes) {
            registerClasses(builder, metaClass.classes().values(), decorator);
            registerMethods(builder, metaClass, decorator);
        }
    }

    private void registerMethods(ImmutableMap.Builder<Class<?>, CommunicatorProxy<?>> builder, MetaClass<?> metaClass, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        CommunicatorProxy<?> proxy = createCommunicatorProxy(metaClass, method -> createAction(metaClass, method, decorator));
        builder.put(metaClass.definition().type(), proxy);
    }

    private CommunicatorAction createAction(MetaClass<?> proxyClass, MetaMethod<?> metaMethod, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        MetaType<?> inputType = orNull(() -> immutableArrayOf(metaMethod.parameters().values()).get(0).type(), isNotEmpty(metaMethod.parameters()));
        CommunicatorActionIdentifier id = communicatorActionId(communicatorId(proxyClass.definition().type()), metaMethod.name());
        CommunicatorActionBuilder builder = CommunicatorAction.builder()
                .id(id)
                .outputType(metaMethod.returnType())
                .targetServiceMethod(serviceMethodId(id.getCommunicatorId(), id.getActionId()))
                .communication(communication.get());
        UnaryOperator<CommunicatorActionBuilder> configurator = decorator.apply(new CommunicatorActionConfigurator(id, configurationProvider.get())).configure();
        if (nonNull(inputType)) {
            return configurator.apply(builder.inputType(inputType)).build();
        }
        return configurator.apply(builder).build();
    }

    @RequiredArgsConstructor
    private static class ClassBasedRegistration {
        final Supplier<MetaClass<?>> proxyClass;
        final UnaryOperator<CommunicatorActionConfigurator> decorator;
    }

    @RequiredArgsConstructor
    private static class PackageBasedRegistration {
        final Supplier<MetaPackage> proxyPackage;
        final UnaryOperator<CommunicatorActionConfigurator> decorator;
    }
}
