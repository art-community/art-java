package io.art.communicator.configurator;

import io.art.communicator.action.*;
import io.art.communicator.action.CommunicatorAction.*;
import io.art.communicator.configuration.*;
import io.art.communicator.model.*;
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
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
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
    private final List<ClassBasedRegistration> classBased = linkedList();


    public CommunicatorConfigurator configureProxy(Class<?> proxyClass) {
        return configureProxy(() -> declaration(proxyClass), identity());
    }

    public CommunicatorConfigurator configureProxy(Class<?> proxyClass, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        return configureProxy(() -> declaration(proxyClass), decorator);
    }

    public CommunicatorConfigurator configureProxy(Supplier<MetaClass<?>> proxyClass, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        classBased.add(new ClassBasedRegistration(proxyClass, decorator));
        return this;
    }

    protected LazyProperty<ImmutableMap<Class<?>, CommunicatorProxy<?>>> get() {
        return lazy(this::createProxies);
    }

    private ImmutableMap<Class<?>, CommunicatorProxy<?>> createProxies() {
        ImmutableMap.Builder<Class<?>, CommunicatorProxy<?>> proxies = immutableMapBuilder();

        for (ClassBasedRegistration registration : classBased) {
            registerMethods(proxies, registration.proxyClass.get(), registration.decorator);
        }

        return proxies.build();
    }

    private void registerMethods(ImmutableMap.Builder<Class<?>, CommunicatorProxy<?>> builder, MetaClass<?> metaClass, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        CommunicatorProxy<?> proxy = createCommunicatorProxy(metaClass, method -> createAction(metaClass, method, decorator));
        builder.put(metaClass.definition().type(), proxy);
    }

    private CommunicatorAction createAction(MetaClass<?> proxyClass, MetaMethod<?> metaMethod, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        MetaType<?> inputType = orNull(() -> immutableArrayOf(metaMethod.parameters().values()).get(0).type(), isNotEmpty(metaMethod.parameters()));
        CommunicatorActionIdentifier id = communicatorActionId(normalizeToId(proxyClass.definition().type()), metaMethod.name());
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
}
