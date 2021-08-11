package io.art.server.configurator;

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.meta.invoker.*;
import io.art.meta.model.*;
import io.art.server.configuration.*;
import io.art.server.decorator.*;
import io.art.server.method.*;
import io.art.server.method.ServiceMethod.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.extensions.FunctionExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.constants.MetaConstants.MetaTypeModifiers.*;
import static io.art.meta.module.MetaModule.*;
import static java.util.Objects.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public abstract class ServerConfigurator {
    private final List<ClassBasedConfiguration> classBased = linkedList();
    private final List<MethodBasedConfiguration> methodBased = linkedList();


    public ServerConfigurator configureService(Class<?> serviceClass) {
        return configureService(serviceClass, identity());
    }

    public ServerConfigurator configureService(Class<?> serviceClass, UnaryOperator<ServiceMethodConfigurator> decorator) {
        classBased.add(new ClassBasedConfiguration(() -> declaration(serviceClass), decorator));
        return this;
    }

    public <T extends MetaClass<?>>
    ServerConfigurator configureMethod(Class<?> serviceClass, Function<T, MetaMethod<?>> serviceMethod) {
        return configureMethod(serviceClass, serviceMethod, identity());
    }

    public <T extends MetaClass<?>>
    ServerConfigurator configureMethod(Class<?> serviceClass, Function<T, MetaMethod<?>> serviceMethod, UnaryOperator<ServiceMethodConfigurator> decorator) {
        methodBased.add(new MethodBasedConfiguration(() -> declaration(serviceClass), serviceMethod, decorator));
        return this;
    }


    protected ServerConfiguration configure(LazyProperty<ServerConfiguration> configurationProvider, ServerConfiguration current) {
        return current.toBuilder()
                .configurations(lazy(this::createConfigurations))
                .methods(lazy(() -> createMethods(configurationProvider)))
                .build();
    }


    private ImmutableMap<String, ServiceMethodsConfiguration> createConfigurations() {
        Map<String, ServiceMethodsConfiguration> configurations = map();
        for (ClassBasedConfiguration configuration : classBased) {
            MetaClass<?> metaClass = configuration.serviceClass.get();
            Map<String, ServiceMethodConfiguration> methods = map();
            for (MetaMethod<?> method : metaClass.methods()) {
                ServiceMethodConfigurator configurator = configuration.decorator.apply(new ServiceMethodConfigurator());
                methods.put(method.name(), configurator.configure(ServiceMethodConfiguration.defaults()));
            }
            configurations.put(asId(metaClass.definition().type()), ServiceMethodsConfiguration.defaults()
                    .toBuilder()
                    .methods(immutableMapOf(methods))
                    .build());
        }

        for (MethodBasedConfiguration configuration : methodBased) {
            MetaClass<?> metaClass = configuration.serviceClass.get();
            Map<String, ServiceMethodConfiguration> methods = map();
            String communicatorId = asId(metaClass.definition().type());
            ServiceMethodsConfiguration existed = configurations.get(communicatorId);
            if (nonNull(existed)) {
                methods = existed.getMethods().toMutable();
            }
            ServiceMethodConfigurator configurator = configuration.decorator.apply(new ServiceMethodConfigurator());
            String actionId = configuration.serviceMethod.apply(cast(metaClass)).name();
            methods.put(actionId, configurator.configure(ServiceMethodConfiguration.defaults()));
            configurations.put(communicatorId, orElse(existed, ServiceMethodsConfiguration.defaults()).toBuilder()
                    .methods(immutableMapOf(methods))
                    .build());
        }

        return immutableMapOf(configurations);
    }

    private ImmutableMap<ServiceMethodIdentifier, ServiceMethod> createMethods(LazyProperty<ServerConfiguration> configurationProvider) {
        Map<ServiceMethodIdentifier, ServiceMethod> methods = map();

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
                ServiceMethod serviceMethod = createMethod(configurationProvider, methodConfiguration);
                methods.put(serviceMethod.getId(), serviceMethod);
            }
        }

        for (MethodBasedConfiguration configuration : methodBased) {
            MetaClass<?> serviceClass = configuration.serviceClass.get();
            MetaMethod<?> method = configuration.serviceMethod.apply(cast(serviceClass));
            UnaryOperator<ServiceMethodConfigurator> decorator = classBased
                    .stream()
                    .filter(methodConfiguration -> serviceClass.equals(methodConfiguration.serviceClass.get()))
                    .findFirst()
                    .map(methodConfiguration -> methodConfiguration.decorator)
                    .orElse(identity());
            decorator = then(configuration.decorator, decorator);
            MethodConfiguration methodConfiguration = new MethodConfiguration(serviceClass, method, decorator);
            ServiceMethod serviceMethod = createMethod(configurationProvider, methodConfiguration);
            methods.put(serviceMethod.getId(), serviceMethod);
        }

        return immutableMapOf(methods);
    }

    private ServiceMethod createMethod(LazyProperty<ServerConfiguration> configurationProvider, MethodConfiguration methodConfiguration) {
        MetaMethod<?> serviceMethod = methodConfiguration.serviceMethod;
        MetaClass<?> serviceClass = methodConfiguration.serviceClass;
        MetaType<?> inputType = orNull(() -> immutableArrayOf(serviceMethod.parameters().values()).get(0).type(), isNotEmpty(serviceMethod.parameters()));
        ServiceMethodIdentifier id = serviceMethodId(asId(serviceClass.definition().type()), serviceMethod.name());
        ServiceMethodBuilder builder = ServiceMethod.builder()
                .id(id)
                .outputType(serviceMethod.returnType())
                .invoker(new MetaMethodInvoker(serviceClass, serviceMethod));
        if (nonNull(inputType)) {
            builder.inputType(inputType);
        }
        ServerConfiguration serverConfiguration = configurationProvider.get();
        boolean deactivated = serverConfiguration.isDeactivated(id);
        boolean validating = serverConfiguration.isValidating(id);
        boolean logging = serverConfiguration.isLogging(id);

        if (deactivated) {
            builder.inputDecorator(new ServiceDeactivationDecorator(id, serverConfiguration));
        }

        if (logging) {
            builder.inputDecorator(new ServiceLoggingDecorator(id, serverConfiguration, INPUT));
        }

        if (validating && nonNull(inputType) && inputType.modifiers().contains(VALIDATABLE)) {
            builder.inputDecorator(new ServiceValidationDecorator(id, serverConfiguration));
        }

        ServiceMethodsConfiguration serviceConfiguration = serverConfiguration.getConfigurations().get().get(id.getServiceId());
        if (nonNull(serviceConfiguration)) {
            serviceConfiguration.getInputDecorators().forEach(builder::inputDecorator);
            ServiceMethodConfiguration serviceMethodConfiguration = serviceConfiguration.getMethods().get(id.getMethodId());
            if (nonNull(serviceMethodConfiguration)) {
                serviceMethodConfiguration.getInputDecorators().forEach(builder::inputDecorator);
            }
        }

        if (deactivated) {
            builder.outputDecorator(new ServiceDeactivationDecorator(id, serverConfiguration));
        }

        if (logging) {
            builder.outputDecorator(new ServiceLoggingDecorator(id, serverConfiguration, OUTPUT));
        }

        if (nonNull(serviceConfiguration)) {
            serviceConfiguration.getOutputDecorators().forEach(builder::outputDecorator);
            ServiceMethodConfiguration serviceMethodConfiguration = serviceConfiguration.getMethods().get(id.getMethodId());
            if (nonNull(serviceMethodConfiguration)) {
                serviceMethodConfiguration.getOutputDecorators().forEach(builder::outputDecorator);
            }
        }

        return builder.build();
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
