package io.art.server.configurator;

import io.art.core.annotation.*;
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
import static io.art.meta.Meta.*;
import static io.art.meta.constants.MetaConstants.MetaTypeModifiers.*;
import static java.util.Objects.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
@RequiredArgsConstructor
public abstract class ServerConfigurator<S extends ServerConfigurator<S>> {
    private final List<ClassBasedConfiguration> classBased = linkedList();
    private final List<MethodBasedConfiguration> methodBased = linkedList();


    public S service(Class<?> serviceClass) {
        return service(serviceClass, identity());
    }

    public S service(Class<?> serviceClass, UnaryOperator<ServiceMethodConfigurator> decorator) {
        classBased.add(new ClassBasedConfiguration(() -> declaration(serviceClass), decorator));
        return cast(this);
    }

    public <T extends MetaClass<?>>
    S method(Class<?> serviceClass, Function<T, MetaMethod<?>> serviceMethod) {
        return method(serviceClass, serviceMethod, identity());
    }

    public <T extends MetaClass<?>>
    S method(Class<?> serviceClass, Function<T, MetaMethod<?>> serviceMethod, UnaryOperator<ServiceMethodConfigurator> decorator) {
        methodBased.add(new MethodBasedConfiguration(() -> declaration(serviceClass), serviceMethod, decorator));
        return cast(this);
    }


    protected ServerConfiguration configure(LazyProperty<ServerConfiguration> configurationProvider, ServerConfiguration current) {
        return current.toBuilder()
                .configurations(lazy(this::createConfigurations))
                .methods(lazy(() -> createMethods(configurationProvider)))
                .build();
    }

    private ImmutableMap<String, ServiceMethodsConfiguration> createConfigurations() {
        Map<String, ServiceMethodsConfiguration> configurations = map();
        for (ClassBasedConfiguration classBasedConfiguration : classBased) {
            MetaClass<?> serviceClass = classBasedConfiguration.serviceClass.get();
            Map<String, ServiceMethodConfiguration> methods = map();
            for (MetaMethod<?> method : serviceClass.methods()) {
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
            MetaMethod<?> method = methodBasedConfiguration.serviceMethod.apply(cast(serviceClass));
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
            for (MetaMethod<?> method : serviceClass.methods()) {
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
            MetaMethod<?> method = methodBasedConfiguration.serviceMethod.apply(cast(serviceClass));
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

    private UnaryOperator<ServiceMethodConfigurator> getMethodDecorator(MetaClass<?> serviceClass, MetaMethod<?> method) {
        return methodBased
                .stream()
                .filter(methodConfiguration -> serviceClass.equals(methodConfiguration.serviceClass.get()))
                .filter(methodConfiguration -> method.equals(methodConfiguration.serviceMethod.apply(cast(methodConfiguration.serviceClass.get()))))
                .map(methodConfiguration -> methodConfiguration.decorator)
                .reduce(FunctionExtensions::then)
                .orElse(identity());
    }

    private ServiceMethod createMethod(LazyProperty<ServerConfiguration> configurationProvider, MethodConfiguration methodConfiguration) {
        MetaMethod<?> serviceMethod = methodConfiguration.serviceMethod;
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

        if (withLogging()) {
            builder.inputDecorator(new ServiceLoggingDecorator(id, serverConfiguration, INPUT));
        }

        if (nonNull(inputType) && inputType.modifiers().contains(VALIDATABLE)) {
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

        builder.outputDecorator(new ServiceDeactivationDecorator(id, serverConfiguration));

        if (withLogging()) {
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
