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
import static io.art.communicator.factory.ConnectorProxyFactory.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.model.CommunicatorActionIdentifier.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.module.MetaModule.*;
import static java.util.Objects.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public abstract class CommunicatorConfigurator {
    private final List<ClassBasedConfiguration> classBased = linkedList();
    private final Map<Class<? extends Connector>, LazyProperty<? extends Connector>> connectors = map();

    public CommunicatorConfigurator configure(Class<? extends Communicator> communicatorClass) {
        return configure(() -> declaration(communicatorClass), identity());
    }

    public CommunicatorConfigurator configure(Class<? extends Communicator> communicatorClass, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        return configure(() -> declaration(communicatorClass), decorator);
    }

    public CommunicatorConfigurator configure(Supplier<MetaClass<? extends Communicator>> communicatorClass, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        classBased.add(new ClassBasedConfiguration(communicatorClass, decorator));
        return this;
    }


    protected void registerConnector(Class<? extends Connector> connectorClass, Function<Class<? extends Communicator>, ? extends Communicator> communicatorFactory) {
        connectors.put(connectorClass, lazy(() -> createConnectorProxy(declaration(connectorClass), communicatorFactory)));
    }

    protected CommunicatorConfiguration configure(CommunicatorConfiguration current) {
        return current.toBuilder()
                .communicators(createCommunicators(current))
                .connectors(createConnectors())
                .build();
    }

    private LazyProperty<ImmutableMap<Class<? extends Communicator>, ? extends Communicator>> createCommunicators(CommunicatorConfiguration communicatorConfiguration) {
        return lazy(() -> createProxies(communicatorConfiguration));
    }

    private LazyProperty<ImmutableMap<Class<? extends Connector>, ? extends Connector>> createConnectors() {
        return lazy(() -> connectors
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get())));
    }


    private ImmutableMap<Class<? extends Communicator>, ? extends Communicator> createProxies(CommunicatorConfiguration communicatorConfiguration) {
        ImmutableMap.Builder<Class<? extends Communicator>, ? extends Communicator> proxies = immutableMapBuilder();
        Map<MetaClass<? extends Communicator>, UnaryOperator<CommunicatorActionConfigurator>> classBasedConfigurations = classBased
                .stream()
                .collect(mapCollector(configuration -> configuration.communicatorClass.get(), configuration -> configuration.decorator));

        for (Class<? extends Connector> connector : connectors.keySet()) {
            ImmutableSet<MetaMethod<? extends Communicator>> methods = cast(declaration(connector).methods());
            for (MetaMethod<? extends Communicator> method : methods) {
                MetaClass<? extends Communicator> communicatorClass = method.returnType().declaration();
                UnaryOperator<CommunicatorActionConfigurator> decorator = orElse(classBasedConfigurations.get(communicatorClass), identity());
                Function<MetaMethod<?>, CommunicatorAction> provider = actionMethod -> createAction(communicatorConfiguration, new ActionConfiguration(communicatorClass, decorator, actionMethod));
                Communicator communicator = createCommunicatorProxy(communicatorClass, provider);
                proxies.put(communicatorClass.definition().type(), cast(communicator));
            }
        }

        return proxies.build();
    }

    private CommunicatorAction createAction(CommunicatorConfiguration communicatorConfiguration, ActionConfiguration actionConfiguration) {
        MetaClass<? extends Communicator> communicatorClass = actionConfiguration.communicatorClass;
        ImmutableMap<String, MetaParameter<?>> parameters = actionConfiguration.method.parameters();
        MetaType<?> inputType = orNull(() -> immutableArrayOf(parameters.values()).get(0).type(), isNotEmpty(parameters));
        UnaryOperator<CommunicatorActionConfigurator> decorator = actionConfiguration.decorator;
        CommunicatorActionIdentifier id = communicatorActionId(asId(communicatorClass.definition().type()), actionConfiguration.method.name());
        CommunicatorActionBuilder builder = CommunicatorAction.builder()
                .id(id)
                .outputType(actionConfiguration.method.returnType())
                .communication(communicatorConfiguration.getCommunications().get().get(id));
        builder = decorator.apply(new CommunicatorActionConfigurator(id, communicatorConfiguration)).configure(builder);
        return nonNull(inputType) ? builder.inputType(inputType).build() : builder.build();
    }

    @RequiredArgsConstructor
    private static class ClassBasedConfiguration {
        final Supplier<MetaClass<? extends Communicator>> communicatorClass;
        final UnaryOperator<CommunicatorActionConfigurator> decorator;
    }

    @RequiredArgsConstructor
    private static class ActionConfiguration {
        final MetaClass<? extends Communicator> communicatorClass;
        final UnaryOperator<CommunicatorActionConfigurator> decorator;
        final MetaMethod<?> method;
    }
}
