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
    private final LazyProperty<CommunicatorConfiguration> configuration;
    private final LazyProperty<Map<CommunicatorActionIdentifier, Communication>> communications;
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

    protected LazyProperty<ImmutableMap<Class<? extends Communicator>, ? extends Communicator>> createCommunicators() {
        return lazy(this::createProxies);
    }

    protected LazyProperty<ImmutableMap<Class<? extends Connector>, ? extends Connector>> createConnectors() {
        return lazy(() -> connectors
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get())));
    }


    private ImmutableMap<Class<? extends Communicator>, ? extends Communicator> createProxies() {
        ImmutableMap.Builder<Class<? extends Communicator>, ? extends Communicator> proxies = immutableMapBuilder();
        Map<MetaClass<? extends Communicator>, UnaryOperator<CommunicatorActionConfigurator>> classBasedConfigurations = classBased
                .stream()
                .collect(mapCollector(configuration -> configuration.communicatorClass.get(), configuration -> configuration.decorator));

        for (Class<? extends Connector> connector : connectors.keySet()) {
            for (MetaMethod<?> method : declaration(connector).methods()) {
                MetaClass<?> communicatorClass = method.returnType().declaration();
                registerProxies(proxies, cast(communicatorClass), orElse(classBasedConfigurations.get(communicatorClass), identity()));
            }
        }

        return proxies.build();
    }

    private void registerProxies(ImmutableMap.Builder<Class<? extends Communicator>, ? extends Communicator> builder,
                                 MetaClass<? extends Communicator> communicatorClass,
                                 UnaryOperator<CommunicatorActionConfigurator> decorator) {
        Communicator communicator = createCommunicatorProxy(communicatorClass, method -> createAction(communicatorClass, method, decorator));
        builder.put(communicatorClass.definition().type(), cast(communicator));
    }

    private CommunicatorAction createAction(MetaClass<?> communicatorClass, MetaMethod<?> method, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        MetaType<?> inputType = orNull(() -> immutableArrayOf(method.parameters().values()).get(0).type(), isNotEmpty(method.parameters()));
        CommunicatorActionIdentifier id = communicatorActionId(asId(communicatorClass.definition().type()), method.name());
        CommunicatorActionBuilder builder = CommunicatorAction.builder()
                .id(id)
                .outputType(method.returnType())
                .communication(communications.get().get(id));
        builder = decorator.apply(new CommunicatorActionConfigurator(id, configuration.get())).configure(builder);
        return nonNull(inputType) ? builder.inputType(inputType).build() : builder.build();
    }


    @RequiredArgsConstructor
    private static class ClassBasedConfiguration {
        final Supplier<MetaClass<? extends Communicator>> communicatorClass;
        final UnaryOperator<CommunicatorActionConfigurator> decorator;
    }
}
