package io.art.server.configurator;

import io.art.core.checker.*;
import io.art.core.collection.*;
import io.art.core.extensions.*;
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
import static io.art.core.checker.ModuleChecker.*;
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
import static java.util.Objects.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public class ServerConfiguratorImplementation implements ServerConfigurator {
    private final List<ClassBasedConfiguration> classBased = linkedList();
    private final List<MethodBasedConfiguration> methodBased = linkedList();

    @Override
    public <M extends MetaClass<?>> ServerConfigurator service(Supplier<M> serviceClass, UnaryOperator<ServiceMethodConfigurator> decorator) {
        classBased.add(new ClassBasedConfiguration(serviceClass, decorator));
        return cast(this);
    }

    @Override
    public <M extends MetaClass<?>> ServerConfigurator method(Supplier<MetaMethod<M, ?>> serviceMethod, UnaryOperator<ServiceMethodConfigurator> decorator) {
        methodBased.add(new MethodBasedConfiguration(() -> serviceMethod.get().owner(), cast(serviceMethod), decorator));
        return cast(this);
    }

    public ServerConfiguration createConfiguration(LazyProperty<ServerConfiguration> configurationProvider, ServerConfiguration current) {
        return current.toBuilder()
                .configurations(lazy(this::createMethodsConfigurations))
                .methods(lazy(() -> createMethods(configurationProvider)))
                .build();
    }

    private ImmutableMap<String, ServiceMethodsConfiguration> createMethodsConfigurations() {
        Map<String, ServiceMethodsConfiguration> configurations = map();
        for (ClassBasedConfiguration classBasedConfiguration : classBased) {
            MetaClass<?> serviceClass = classBasedConfiguration.serviceClass.get();
            Map<String, ServiceMethodConfiguration> methods = map();
            for (MetaMethod<MetaClass<?>, ?> method : serviceClass.methods()) {
                if (!method.isKnown()) continue;
                UnaryOperator<ServiceMethodConfigurator> decorator = getMethodDecorator(serviceClass, method);
                decorator = then(getServiceDecorator(serviceClass), decorator);
                ServiceMethodConfigurator configurator = decorator.apply(new ServiceMethodConfigurator());
                methods.put(method.name(), configurator.configure(ServiceMethodConfiguration.defaults()));
            }
            configurations.put(idByDash(serviceClass.definition().type()), ServiceMethodsConfiguration.defaults()
                    .toBuilder()
                    .methods(immutableMapOf(methods))
                    .build());
        }

        for (MethodBasedConfiguration methodBasedConfiguration : methodBased) {
            MetaClass<?> serviceClass = methodBasedConfiguration.serviceClass.get();
            Map<String, ServiceMethodConfiguration> methods = map();
            String communicatorId = idByDash(serviceClass.definition().type());
            ServiceMethodsConfiguration existed = configurations.get(communicatorId);
            if (nonNull(existed)) methods = existed.getMethods().toMutable();
            MetaMethod<MetaClass<?>, ?> method = methodBasedConfiguration.serviceMethod.get();
            if (!method.isKnown()) continue;
            UnaryOperator<ServiceMethodConfigurator> decorator = getServiceDecorator(serviceClass);
            decorator = then(decorator, getMethodDecorator(serviceClass, method));
            ServiceMethodConfigurator configurator = decorator.apply(new ServiceMethodConfigurator());
            methods.put(method.name(), configurator.configure(ServiceMethodConfiguration.defaults()));
            configurations.put(communicatorId, orElse(existed, ServiceMethodsConfiguration.defaults()).toBuilder()
                    .methods(immutableMapOf(methods))
                    .build());
        }

        return immutableMapOf(configurations);
    }

    private ImmutableMap<ServiceMethodIdentifier, ServiceMethod> createMethods(LazyProperty<ServerConfiguration> configurationProvider) {
        Map<ServiceMethodIdentifier, ServiceMethod> methods = map();

        for (ClassBasedConfiguration classBasedConfiguration : classBased) {
            MetaClass<?> serviceClass = classBasedConfiguration.serviceClass.get();
            for (MetaMethod<MetaClass<?>, ?> method : serviceClass.methods()) {
                UnaryOperator<ServiceMethodConfigurator> decorator = getMethodDecorator(serviceClass, method);
                decorator = then(getServiceDecorator(serviceClass), decorator);
                MethodConfiguration methodConfiguration = new MethodConfiguration(serviceClass, method, decorator);
                if (!method.isKnown()) continue;
                ServiceMethod serviceMethod = createMethod(configurationProvider, methodConfiguration);
                methods.put(serviceMethod.getId(), serviceMethod);
            }
        }

        for (MethodBasedConfiguration methodBasedConfiguration : methodBased) {
            MetaClass<?> serviceClass = methodBasedConfiguration.serviceClass.get();
            MetaMethod<MetaClass<?>, ?> method = methodBasedConfiguration.serviceMethod.get();
            UnaryOperator<ServiceMethodConfigurator> decorator = getServiceDecorator(serviceClass);
            decorator = then(decorator, getMethodDecorator(serviceClass, method));
            MethodConfiguration methodConfiguration = new MethodConfiguration(serviceClass, method, decorator);
            if (!method.isKnown()) continue;
            ServiceMethod serviceMethod = createMethod(configurationProvider, methodConfiguration);
            methods.put(serviceMethod.getId(), serviceMethod);
        }

        return immutableMapOf(methods);
    }

    private UnaryOperator<ServiceMethodConfigurator> getServiceDecorator(MetaClass<?> serviceClass) {
        return classBased
                .stream()
                .filter(classConfiguration -> serviceClass.equals(classConfiguration.serviceClass.get()))
                .map(classConfiguration -> classConfiguration.decorator)
                .reduce(FunctionExtensions::then)
                .orElse(identity());
    }

    private UnaryOperator<ServiceMethodConfigurator> getMethodDecorator(MetaClass<?> serviceClass, MetaMethod<MetaClass<?>, ?> method) {
        return methodBased
                .stream()
                .filter(methodConfiguration -> serviceClass.equals(methodConfiguration.serviceClass.get()))
                .filter(methodConfiguration -> method.equals(methodConfiguration.serviceMethod))
                .map(methodConfiguration -> methodConfiguration.decorator)
                .reduce(FunctionExtensions::then)
                .orElse(identity());
    }

    private ServiceMethod createMethod(LazyProperty<ServerConfiguration> configurationProvider, MethodConfiguration methodConfiguration) {
        MetaMethod<MetaClass<?>, ?> serviceMethod = methodConfiguration.serviceMethod;
        MetaClass<?> serviceClass = methodConfiguration.serviceClass;
        MetaType<?> inputType = orNull(() -> immutableArrayOf(serviceMethod.parameters().values()).get(0).type(), isNotEmpty(serviceMethod.parameters()));
        ServiceMethodIdentifier id = serviceMethodId(idByDash(serviceClass.definition().type()), serviceMethod.name());
        ServiceMethodBuilder builder = ServiceMethod.builder()
                .id(id)
                .outputType(serviceMethod.returnType())
                .invoker(new MetaMethodInvoker(serviceClass, serviceMethod));
        NullityChecker.apply(inputType, builder::inputType);

        ServerConfiguration serverConfiguration = configurationProvider.get();
        builder.inputDecorator(new ServiceDeactivationDecorator(id, serverConfiguration));
        builder.outputDecorator(new ServiceDeactivationDecorator(id, serverConfiguration));

        if (withLogging()) {
            builder.inputDecorator(new ServiceLoggingDecorator(id, serverConfiguration, INPUT));
            builder.outputDecorator(new ServiceLoggingDecorator(id, serverConfiguration, OUTPUT));
        }

        if (nonNull(inputType) && inputType.modifiers().contains(VALIDATABLE)) {
            builder.inputDecorator(new ServiceValidationDecorator(id, serverConfiguration));
        }

        ServiceMethodsConfiguration serviceConfiguration = serverConfiguration.getConfigurations().get().get(id.getServiceId());
        if (nonNull(serviceConfiguration)) {
            serviceConfiguration.getInputDecorators().forEach(builder::inputDecorator);
            serviceConfiguration.getOutputDecorators().forEach(builder::outputDecorator);
            ServiceMethodConfiguration serviceMethodConfiguration = serviceConfiguration.getMethods().get(id.getMethodId());
            if (nonNull(serviceMethodConfiguration)) {
                serviceMethodConfiguration.getInputDecorators().forEach(builder::inputDecorator);
                serviceMethodConfiguration.getOutputDecorators().forEach(builder::outputDecorator);
            }
        }

        return builder.build();
    }


    @RequiredArgsConstructor
    private static class ClassBasedConfiguration {
        final Supplier<? extends MetaClass<?>> serviceClass;
        final UnaryOperator<ServiceMethodConfigurator> decorator;
    }

    @RequiredArgsConstructor
    private static class MethodBasedConfiguration {
        final Supplier<? extends MetaClass<?>> serviceClass;
        final Supplier<MetaMethod<MetaClass<?>, ?>> serviceMethod;
        final UnaryOperator<ServiceMethodConfigurator> decorator;
    }

    @RequiredArgsConstructor
    private static class MethodConfiguration {
        final MetaClass<?> serviceClass;
        final MetaMethod<MetaClass<?>, ?> serviceMethod;
        final UnaryOperator<ServiceMethodConfigurator> decorator;
    }
}
