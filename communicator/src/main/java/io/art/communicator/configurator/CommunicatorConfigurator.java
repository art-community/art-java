package io.art.communicator.configurator;

import io.art.communicator.action.*;
import io.art.communicator.action.CommunicatorAction.*;
import io.art.communicator.configuration.*;
import io.art.communicator.decorator.*;
import io.art.communicator.model.*;
import io.art.communicator.registry.*;
import io.art.communicator.registry.ConnectorRegistry.*;
import io.art.core.checker.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import io.art.resilience.configuration.*;
import lombok.Builder;
import lombok.*;
import static io.art.communicator.factory.CommunicatorProxyFactory.*;
import static io.art.communicator.factory.ConnectorProxyFactory.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.extensions.FunctionExtensions.*;
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
    private final List<MethodBasedConfiguration> methodBased = linkedList();
    private final Map<Class<? extends Connector>, ConnectorConfiguration> connectors = map();


    public CommunicatorConfigurator configureCommunicator(Class<? extends Communicator> communicatorClass,
                                                          UnaryOperator<CommunicatorActionConfigurator> decorator) {
        classBased.add(new ClassBasedConfiguration(() -> declaration(communicatorClass), decorator));
        return this;
    }

    public <T extends MetaClass<? extends Communicator>>
    CommunicatorConfigurator configureCommunicator(Class<? extends Communicator> communicatorClass,
                                                   Function<T, MetaMethod<?>> actionMethod,
                                                   UnaryOperator<CommunicatorActionConfigurator> decorator) {
        methodBased.add(new MethodBasedConfiguration(() -> declaration(communicatorClass), actionMethod, decorator));
        return this;
    }


    protected void registerConnector(Class<? extends Connector> connectorClass,
                                     Function<Class<? extends Communicator>, ? extends Communicator> communicator,
                                     Function<CommunicatorActionIdentifier, ? extends Communication> communication) {
        LazyProperty<? extends Connector> connector = lazy(() -> createConnectorProxy(declaration(connectorClass), communicator));
        ConnectorConfiguration connectorConfiguration = new ConnectorConfiguration(connector, communication);
        connectors.put(connectorClass, connectorConfiguration);
    }

    protected CommunicatorConfiguration configure(LazyProperty<CommunicatorConfiguration> configurationProvider, CommunicatorConfiguration current) {
        return current.toBuilder()
                .configurations(lazy(this::createConfigurations))
                .connectors(new ConnectorRegistry(createConnectors(configurationProvider)))
                .build();
    }


    private LazyProperty<ImmutableMap<Class<? extends Connector>, ConnectorContainer>> createConnectors(LazyProperty<CommunicatorConfiguration> configurationProvider) {
        return lazy(() -> connectors
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> createConnector(configurationProvider, entry.getKey(), entry.getValue()))));
    }

    private ImmutableMap<String, CommunicatorActionsConfiguration> createConfigurations() {
        Map<String, CommunicatorActionsConfiguration> configurations = map();
        for (ClassBasedConfiguration configuration : classBased) {
            MetaClass<? extends Communicator> communicatorClass = configuration.communicatorClass.get();
            Map<String, CommunicatorActionConfiguration> actions = map();
            for (MetaMethod<?> method : communicatorClass.methods()) {
                UnaryOperator<CommunicatorActionConfigurator> decorator = getActionDecorator(communicatorClass, method);
                decorator = then(configuration.decorator, decorator);
                CommunicatorActionConfigurator configurator = decorator.apply(new CommunicatorActionConfigurator());
                actions.put(method.name(), configurator.configure(CommunicatorActionConfiguration.defaults()));
            }
            configurations.put(asId(communicatorClass.definition().type()), CommunicatorActionsConfiguration.defaults()
                    .toBuilder()
                    .actions(immutableMapOf(actions))
                    .build());
        }

        for (MethodBasedConfiguration configuration : methodBased) {
            MetaClass<? extends Communicator> communicatorClass = configuration.communicatorClass.get();
            Map<String, CommunicatorActionConfiguration> actions = map();
            String communicatorId = asId(communicatorClass.definition().type());
            CommunicatorActionsConfiguration existed = configurations.get(communicatorId);
            if (nonNull(existed)) {
                actions = existed.getActions().toMutable();
            }
            MetaMethod<?> method = configuration.actionMethod.apply(cast(communicatorClass));
            UnaryOperator<CommunicatorActionConfigurator> decorator = getCommunicatorDecorator(communicatorClass);
            decorator = then(decorator, configuration.decorator);
            CommunicatorActionConfigurator configurator = decorator.apply(new CommunicatorActionConfigurator());
            actions.put(method.name(), configurator.configure(CommunicatorActionConfiguration.defaults()));
            configurations.put(communicatorId, orElse(existed, CommunicatorActionsConfiguration.defaults()).toBuilder()
                    .actions(immutableMapOf(actions))
                    .build());
        }

        return immutableMapOf(configurations);
    }

    private ConnectorContainer createConnector(LazyProperty<CommunicatorConfiguration> configurationProvider,
                                               Class<? extends Connector> connectorClass,
                                               ConnectorConfiguration connectorConfiguration) {
        ImmutableMap<Class<? extends Communicator>, CommunicatorProxy<? extends Communicator>> communicators = createProxies(configurationProvider, connectorClass, connectorConfiguration);
        return new ConnectorContainer(communicators, connectorConfiguration.connector.get());
    }

    private ImmutableMap<Class<? extends Communicator>, CommunicatorProxy<? extends Communicator>> createProxies(LazyProperty<CommunicatorConfiguration> configurationProvider,
                                                                                                                 Class<? extends Connector> connectorClass,
                                                                                                                 ConnectorConfiguration connectorConfiguration) {
        Map<Class<? extends Communicator>, CommunicatorProxy<? extends Communicator>> proxies = map();
        Map<MetaClass<? extends Communicator>, UnaryOperator<CommunicatorActionConfigurator>> classBasedConfigurations = classBased
                .stream()
                .collect(mapCollector(configuration -> configuration.communicatorClass.get(), configuration -> configuration.decorator));
        ImmutableSet<MetaMethod<? extends Communicator>> methods = cast(declaration(connectorClass).methods());
        for (MetaMethod<? extends Communicator> method : methods) {
            MetaClass<? extends Communicator> communicatorClass = method.returnType().declaration();
            Function<MetaMethod<?>, CommunicatorAction> actions = actionMethod -> createAction(configurationProvider, ActionConfiguration.builder()
                    .communicatorClass(communicatorClass)
                    .decorator(computeDecorator(classBasedConfigurations, communicatorClass, actionMethod))
                    .method(actionMethod)
                    .connectorConfiguration(connectorConfiguration)
                    .build());
            CommunicatorProxy<? extends Communicator> communicator = createCommunicatorProxy(communicatorClass, actions);
            proxies.put(communicatorClass.definition().type(), cast(communicator));
        }
        return immutableMapOf(proxies);
    }

    private UnaryOperator<CommunicatorActionConfigurator> computeDecorator(Map<MetaClass<? extends Communicator>, UnaryOperator<CommunicatorActionConfigurator>> classBasedConfigurations, MetaClass<? extends Communicator> communicatorClass, MetaMethod<?> actionMethod) {
        UnaryOperator<CommunicatorActionConfigurator> classDecorator = orElse(classBasedConfigurations.get(communicatorClass), identity());
        UnaryOperator<CommunicatorActionConfigurator> methodDecorator = getActionDecorator(communicatorClass, actionMethod);
        return then(classDecorator, methodDecorator);
    }

    private UnaryOperator<CommunicatorActionConfigurator> getCommunicatorDecorator(MetaClass<?> serviceClass) {
        return classBased
                .stream()
                .filter(classConfiguration -> serviceClass.equals(classConfiguration.communicatorClass.get()))
                .findFirst()
                .map(classConfiguration -> classConfiguration.decorator)
                .orElse(identity());
    }

    private UnaryOperator<CommunicatorActionConfigurator> getActionDecorator(MetaClass<?> serviceClass, MetaMethod<?> method) {
        return methodBased
                .stream()
                .filter(methodConfiguration -> serviceClass.equals(methodConfiguration.communicatorClass.get()))
                .filter(methodConfiguration -> method.equals(methodConfiguration.actionMethod))
                .findFirst()
                .map(methodConfiguration -> methodConfiguration.decorator)
                .orElse(identity());
    }

    private CommunicatorAction createAction(LazyProperty<CommunicatorConfiguration> configurationProvider, ActionConfiguration actionConfiguration) {
        MetaClass<? extends Communicator> communicatorClass = actionConfiguration.communicatorClass;
        ImmutableMap<String, MetaParameter<?>> parameters = actionConfiguration.method.parameters();
        MetaType<?> inputType = orNull(() -> immutableArrayOf(parameters.values()).get(0).type(), isNotEmpty(parameters));
        CommunicatorActionIdentifier id = communicatorActionId(asId(communicatorClass.definition().type()), actionConfiguration.method.name());
        CommunicatorActionBuilder builder = CommunicatorAction.builder()
                .id(id)
                .outputType(actionConfiguration.method.returnType())
                .communication(actionConfiguration.connectorConfiguration.communication.apply(id));
        NullityChecker.apply(inputType, builder::inputType);

        CommunicatorConfiguration communicatorConfiguration = configurationProvider.get();
        boolean deactivated = communicatorConfiguration.isDeactivated(id);
        boolean logging = communicatorConfiguration.isLogging(id);
        Optional<ResilienceConfiguration> resilience = communicatorConfiguration.getResilienceConfiguration(id);

        if (deactivated) {
            builder.inputDecorator(new CommunicatorDeactivationDecorator(id, communicatorConfiguration));
        }

        if (logging) {
            builder.inputDecorator(new CommunicatorLoggingDecorator(id, communicatorConfiguration, INPUT));
        }

        if (resilience.isPresent()) {
            builder.inputDecorator(new CommunicatorResilienceDecorator(id, communicatorConfiguration));
        }

        CommunicatorActionsConfiguration actionsConfiguration = communicatorConfiguration.getConfigurations().get().get(id.getCommunicatorId());
        if (nonNull(actionsConfiguration)) {
            actionsConfiguration.getInputDecorators().forEach(builder::inputDecorator);
            CommunicatorActionConfiguration communicatorActionConfiguration = actionsConfiguration.getActions().get(id.getActionId());
            if (nonNull(communicatorActionConfiguration)) {
                communicatorActionConfiguration.getInputDecorators().forEach(builder::inputDecorator);
            }
        }

        if (deactivated) {
            builder.outputDecorator(new CommunicatorDeactivationDecorator(id, communicatorConfiguration));
        }

        if (logging) {
            builder.outputDecorator(new CommunicatorLoggingDecorator(id, communicatorConfiguration, OUTPUT));
        }

        if (nonNull(actionsConfiguration)) {
            actionsConfiguration.getOutputDecorators().forEach(builder::outputDecorator);
            CommunicatorActionConfiguration communicatorActionConfiguration = actionsConfiguration.getActions().get(id.getActionId());
            if (nonNull(communicatorActionConfiguration)) {
                communicatorActionConfiguration.getOutputDecorators().forEach(builder::outputDecorator);
            }
        }

        return builder.build();
    }

    @RequiredArgsConstructor
    private static class ClassBasedConfiguration {
        final Supplier<MetaClass<? extends Communicator>> communicatorClass;
        final UnaryOperator<CommunicatorActionConfigurator> decorator;
    }

    @RequiredArgsConstructor
    private static class MethodBasedConfiguration {
        final Supplier<? extends MetaClass<? extends Communicator>> communicatorClass;
        final Function<? extends MetaClass<? extends Communicator>, MetaMethod<?>> actionMethod;
        final UnaryOperator<CommunicatorActionConfigurator> decorator;
    }

    @Builder
    private static class ActionConfiguration {
        final MetaClass<? extends Communicator> communicatorClass;
        final UnaryOperator<CommunicatorActionConfigurator> decorator;
        final MetaMethod<?> method;
        final ConnectorConfiguration connectorConfiguration;
    }

    @RequiredArgsConstructor
    private static class ConnectorConfiguration {
        final LazyProperty<? extends Connector> connector;
        final Function<CommunicatorActionIdentifier, ? extends Communication> communication;
    }
}
