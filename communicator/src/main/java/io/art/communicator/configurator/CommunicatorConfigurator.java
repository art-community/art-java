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
    private final Map<Class<? extends Connector>, ConnectorConfiguration> connectors = map();

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


    protected void registerConnector(Class<? extends Connector> connectorClass, Function<Class<? extends Communicator>, ? extends Communicator> communicator, Function<CommunicatorActionIdentifier, ? extends Communication> communication) {
        ConnectorConfiguration connectorConfiguration = new ConnectorConfiguration(lazy(() -> createConnectorProxy(declaration(connectorClass), communicator)), communication);
        connectors.put(connectorClass, connectorConfiguration);
    }

    protected CommunicatorConfiguration configure(LazyProperty<CommunicatorConfiguration> provider, CommunicatorConfiguration current) {
        return current.toBuilder()
                .communicators(createCommunicators(provider))
                .connectors(createConnectors())
                .build();
    }

    private LazyProperty<ImmutableMap<Class<? extends Communicator>, CommunicatorProxy<? extends Communicator>>> createCommunicators(LazyProperty<CommunicatorConfiguration> provider) {
        return lazy(() -> createProxies(provider));
    }

    private LazyProperty<ImmutableMap<Class<? extends Connector>, ? extends Connector>> createConnectors() {
        return lazy(() -> connectors
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().connector.get())));
    }


    private ImmutableMap<Class<? extends Communicator>, CommunicatorProxy<? extends Communicator>> createProxies(LazyProperty<CommunicatorConfiguration> provider) {
        ImmutableMap.Builder<Class<? extends Communicator>, CommunicatorProxy<? extends Communicator>> proxies = immutableMapBuilder();
        Map<MetaClass<? extends Communicator>, UnaryOperator<CommunicatorActionConfigurator>> classBasedConfigurations = classBased
                .stream()
                .collect(mapCollector(configuration -> configuration.communicatorClass.get(), configuration -> configuration.decorator));

        for (Map.Entry<Class<? extends Connector>, ConnectorConfiguration> connector : connectors.entrySet()) {
            ImmutableSet<MetaMethod<? extends Communicator>> methods = cast(declaration(connector.getKey()).methods());
            for (MetaMethod<? extends Communicator> method : methods) {
                MetaClass<? extends Communicator> communicatorClass = method.returnType().declaration();
                UnaryOperator<CommunicatorActionConfigurator> decorator = orElse(classBasedConfigurations.get(communicatorClass), identity());
                Function<MetaMethod<?>, CommunicatorAction> action = actionMethod -> createAction(
                        provider,
                        new ActionConfiguration(communicatorClass, decorator, actionMethod),
                        new ConnectorConfiguration(connector.getValue().connector, connector.getValue().communication)
                );
                CommunicatorProxy<? extends Communicator> communicator = createCommunicatorProxy(communicatorClass, action);
                proxies.put(communicatorClass.definition().type(), cast(communicator));
            }
        }

        return proxies.build();
    }

    private CommunicatorAction createAction(LazyProperty<CommunicatorConfiguration> provider, ActionConfiguration actionConfiguration, ConnectorConfiguration connectorConfiguration) {
        MetaClass<? extends Communicator> communicatorClass = actionConfiguration.communicatorClass;
        ImmutableMap<String, MetaParameter<?>> parameters = actionConfiguration.method.parameters();
        MetaType<?> inputType = orNull(() -> immutableArrayOf(parameters.values()).get(0).type(), isNotEmpty(parameters));
        UnaryOperator<CommunicatorActionConfigurator> decorator = actionConfiguration.decorator;
        CommunicatorActionIdentifier id = communicatorActionId(asId(communicatorClass.definition().type()), actionConfiguration.method.name());
        CommunicatorConfiguration communicatorConfiguration = provider.get();
        CommunicatorActionBuilder builder = CommunicatorAction.builder()
                .id(id)
                .outputType(actionConfiguration.method.returnType())
                .communication(connectorConfiguration.communication.apply(id));
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

    @RequiredArgsConstructor
    private static class ConnectorConfiguration {
        final LazyProperty<? extends Connector> connector;
        final Function<CommunicatorActionIdentifier, ? extends Communication> communication;
    }
}
