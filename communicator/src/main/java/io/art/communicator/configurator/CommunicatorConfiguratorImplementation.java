package io.art.communicator.configurator;

import io.art.communicator.*;
import io.art.communicator.action.*;
import io.art.communicator.action.CommunicatorAction.*;
import io.art.communicator.configuration.*;
import io.art.communicator.decorator.*;
import io.art.communicator.model.*;
import io.art.communicator.registry.*;
import io.art.core.checker.*;
import io.art.core.collection.*;
import io.art.core.extensions.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import lombok.*;
import static io.art.communicator.factory.CommunicatorProxyFactory.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.extensions.FunctionExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.model.CommunicatorActionIdentifier.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.Meta.*;
import static java.util.Objects.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

public class CommunicatorConfiguratorImplementation implements CommunicatorConfigurator {
    private final List<ClassBasedConfiguration> classBased = linkedList();
    private final List<MethodBasedConfiguration> methodBased = linkedList();
    private final Map<CommunicatorKey, CommunicatorActionFactory> communicators = map();

    @Override
    public CommunicatorConfigurator communicator(Class<?> communicatorClass, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        return communicator(() -> cast(declaration(communicatorClass)), decorator);
    }

    @Override
    public <M extends MetaClass<? extends Communicator>> CommunicatorConfigurator
    communicator(Supplier<M> communicatorClass, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        classBased.add(new ClassBasedConfiguration(communicatorClass, cast(decorator)));
        return this;
    }

    @Override
    public <M extends MetaClass<? extends Communicator>> CommunicatorConfigurator
    action(Supplier<M> communicatorClass, Supplier<MetaMethod<M, ?>> actionMethod, UnaryOperator<CommunicatorActionConfigurator> decorator) {
        methodBased.add(new MethodBasedConfiguration(communicatorClass, cast(actionMethod), cast(decorator)));
        return this;
    }

    public CommunicatorConfiguration createConfiguration(LazyProperty<CommunicatorConfiguration> configurationProvider, CommunicatorConfiguration current) {
        return current.toBuilder()
                .configurations(lazy(this::createCommunicatorConfigurations))
                .communicators(new CommunicatorRegistry(lazy(() -> createCommunicatorProxies(configurationProvider))))
                .build();
    }

    public void register(ConnectorIdentifier connector, Class<? extends Communicator> communicatorClass, CommunicatorActionFactory factory) {
        communicators.put(new CommunicatorKey(connector, communicatorClass), factory);
    }

    private ImmutableMap<String, CommunicatorActionsConfiguration> createCommunicatorConfigurations() {
        Map<String, CommunicatorActionsConfiguration> configurations = map();
        for (ClassBasedConfiguration classBasedConfiguration : classBased) {
            MetaClass<? extends Communicator> communicatorClass = classBasedConfiguration.communicatorClass.get();
            Map<String, CommunicatorActionConfiguration> actions = map();
            for (MetaMethod<MetaClass<?>, ?> method : communicatorClass.methods()) {
                if (!method.isKnown()) continue;
                UnaryOperator<CommunicatorActionConfigurator> decorator = getActionDecorator(communicatorClass, method);
                decorator = then(getCommunicatorDecorator(communicatorClass), decorator);
                CommunicatorActionConfigurator configurator = decorator.apply(new CommunicatorActionConfigurator());
                actions.put(method.name(), configurator.createConfiguration(CommunicatorActionConfiguration.defaults()));
            }
            configurations.put(idByDash(communicatorClass.definition().type()), CommunicatorActionsConfiguration.defaults()
                    .toBuilder()
                    .actions(immutableMapOf(actions))
                    .build());
        }

        for (MethodBasedConfiguration methodBasedConfiguration : methodBased) {
            MetaClass<? extends Communicator> communicatorClass = methodBasedConfiguration.communicatorClass.get();
            Map<String, CommunicatorActionConfiguration> actions = map();
            String communicatorId = idByDash(communicatorClass.definition().type());
            CommunicatorActionsConfiguration existed = configurations.get(communicatorId);
            if (nonNull(existed)) actions = existed.getActions().toMutable();
            MetaMethod<MetaClass<?>, ?> method = methodBasedConfiguration.actionMethod.get();
            if (!method.isKnown()) continue;
            UnaryOperator<CommunicatorActionConfigurator> decorator = getCommunicatorDecorator(communicatorClass);
            decorator = then(decorator, getActionDecorator(communicatorClass, method));
            CommunicatorActionConfigurator configurator = decorator.apply(new CommunicatorActionConfigurator());
            actions.put(method.name(), configurator.createConfiguration(CommunicatorActionConfiguration.defaults()));
            configurations.put(communicatorId, orElse(existed, CommunicatorActionsConfiguration.defaults()).toBuilder()
                    .actions(immutableMapOf(actions))
                    .build());
        }

        return immutableMapOf(configurations);
    }

    private ImmutableMap<CommunicatorKey, CommunicatorProxy<? extends Communicator>> createCommunicatorProxies(LazyProperty<CommunicatorConfiguration> configurationProvider) {
        Map<CommunicatorKey, CommunicatorProxy<? extends Communicator>> proxies = map();
        for (Map.Entry<CommunicatorKey, CommunicatorActionFactory> entry : communicators.entrySet()) {
            MetaClass<? extends Communicator> communicatorClass = declaration(entry.getKey().getType());
            CommunicatorActionProvider provider = actionMethod -> createAction(configurationProvider, entry.getValue(), ActionConfiguration.builder()
                    .communicatorClass(communicatorClass)
                    .connector(entry.getKey().getConnector())
                    .decorator(then(getCommunicatorDecorator(communicatorClass), getActionDecorator(communicatorClass, actionMethod)))
                    .method(actionMethod)
                    .build());
            CommunicatorProxy<? extends Communicator> communicator = createCommunicatorProxy(communicatorClass, provider);
            proxies.put(entry.getKey(), cast(communicator));
        }
        return immutableMapOf(proxies);
    }

    private UnaryOperator<CommunicatorActionConfigurator> getCommunicatorDecorator(MetaClass<?> communicatorClass) {
        return classBased
                .stream()
                .filter(classConfiguration -> communicatorClass.equals(classConfiguration.communicatorClass.get()))
                .map(classConfiguration -> classConfiguration.decorator)
                .reduce(FunctionExtensions::then)
                .orElse(identity());
    }

    private UnaryOperator<CommunicatorActionConfigurator> getActionDecorator(MetaClass<?> communicatorClass, MetaMethod<MetaClass<?>, ?> method) {
        return methodBased
                .stream()
                .filter(methodConfiguration -> communicatorClass.equals(methodConfiguration.communicatorClass.get()))
                .filter(methodConfiguration -> method.equals(methodConfiguration.actionMethod))
                .map(methodConfiguration -> methodConfiguration.decorator)
                .reduce(FunctionExtensions::then)
                .orElse(identity());
    }

    private CommunicatorAction createAction(LazyProperty<CommunicatorConfiguration> configurationProvider,
                                            CommunicatorActionFactory communicationFactory,
                                            ActionConfiguration actionConfiguration) {
        MetaClass<? extends Communicator> communicatorClass = actionConfiguration.communicatorClass;
        ImmutableMap<String, MetaParameter<?>> parameters = actionConfiguration.method.parameters();
        MetaType<?> inputType = orNull(() -> immutableArrayOf(parameters.values()).get(0).type(), isNotEmpty(parameters));
        CommunicatorActionIdentifier id = communicatorActionId(idByDash(communicatorClass.definition().type()), actionConfiguration.method.name());
        CommunicatorActionBuilder builder = CommunicatorAction.builder()
                .id(id)
                .outputType(actionConfiguration.method.returnType())
                .owner(communicatorClass)
                .method(actionConfiguration.method)
                .communication(communicationFactory.apply(actionConfiguration.connector, id));
        NullityChecker.apply(inputType, builder::inputType);

        CommunicatorConfiguration communicatorConfiguration = configurationProvider.get();
        builder.inputDecorator(new CommunicatorDeactivationDecorator(id, communicatorConfiguration));
        builder.outputDecorator(new CommunicatorDeactivationDecorator(id, communicatorConfiguration));

        if (withLogging()) {
            builder.inputDecorator(new CommunicatorLoggingDecorator(id, communicatorConfiguration, INPUT));
            builder.outputDecorator(new CommunicatorLoggingDecorator(id, communicatorConfiguration, OUTPUT));
        }

        CommunicatorActionsConfiguration actionsConfiguration = communicatorConfiguration.getConfigurations().get().get(id.getCommunicatorId());
        if (nonNull(actionsConfiguration)) {
            actionsConfiguration.getInputDecorators().forEach(builder::inputDecorator);
            actionsConfiguration.getOutputDecorators().forEach(builder::outputDecorator);
            CommunicatorActionConfiguration communicatorActionConfiguration = actionsConfiguration.getActions().get(id.getActionId());
            if (nonNull(communicatorActionConfiguration)) {
                communicatorActionConfiguration.getInputDecorators().forEach(builder::inputDecorator);
                communicatorActionConfiguration.getOutputDecorators().forEach(builder::outputDecorator);
            }
        }

        return builder.build();
    }

    @RequiredArgsConstructor
    private static class ClassBasedConfiguration {
        final Supplier<? extends MetaClass<? extends Communicator>> communicatorClass;
        final UnaryOperator<CommunicatorActionConfigurator> decorator;
    }

    @RequiredArgsConstructor
    private static class MethodBasedConfiguration {
        final Supplier<? extends MetaClass<? extends Communicator>> communicatorClass;
        final Supplier<MetaMethod<MetaClass<?>, ?>> actionMethod;
        final UnaryOperator<CommunicatorActionConfigurator> decorator;
    }

    @Builder
    private static class ActionConfiguration {
        final ConnectorIdentifier connector;
        final MetaClass<? extends Communicator> communicatorClass;
        final UnaryOperator<CommunicatorActionConfigurator> decorator;
        final MetaMethod<MetaClass<?>, ?> method;
    }
}
