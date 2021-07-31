package io.art.server.registrator;

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.meta.invoker.*;
import io.art.meta.model.*;
import io.art.server.configuration.*;
import io.art.server.method.*;
import io.art.server.method.ServiceMethod.*;
import lombok.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.module.MetaModule.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public abstract class ServerConfigurator {
    private final Supplier<ServerConfiguration> configurationProvider;
    private final List<LazyProperty<ServiceMethod>> methods = linkedList();

    public ServerConfigurator register(MetaPackage servicePackage) {
        return register(servicePackage, UnaryOperator.identity());
    }

    public ServerConfigurator register(Class<?> serviceClass) {
        return register(declaration(serviceClass), UnaryOperator.identity());
    }

    public ServerConfigurator register(MetaClass<?> serviceClass, MetaMethod<?> serviceMethod) {
        return register(serviceClass, serviceMethod, UnaryOperator.identity());
    }

    public ServerConfigurator register(MetaPackage servicePackage, UnaryOperator<ServiceMethodConfigurator> decorator) {
        servicePackage.classes().values().forEach(serviceClass -> register(serviceClass, decorator));
        return this;
    }

    public ServerConfigurator register(Class<?> serviceClass, UnaryOperator<ServiceMethodConfigurator> decorator) {
        MetaClass<?> metaClass = declaration(serviceClass);
        metaClass.methods().forEach(method -> register(metaClass, method, decorator));
        return this;
    }

    public ServerConfigurator register(MetaClass<?> serviceClass, UnaryOperator<ServiceMethodConfigurator> decorator) {
        serviceClass.methods().forEach(method -> register(serviceClass, method, decorator));
        return this;
    }

    public ServerConfigurator register(MetaClass<?> serviceClass, MetaMethod<?> serviceMethod, UnaryOperator<ServiceMethodConfigurator> decorator) {
        methods.add(lazy(() -> createServiceMethod(serviceClass, serviceMethod, decorator)));
        return this;
    }

    protected ImmutableArray<LazyProperty<ServiceMethod>> get() {
        return immutableArrayOf(methods);
    }

    private ServiceMethod createServiceMethod(MetaClass<?> serviceClass, MetaMethod<?> serviceMethod, UnaryOperator<ServiceMethodConfigurator> decorator) {
        MetaType<?> inputType = orNull(() -> immutableArrayOf(serviceMethod.parameters().values()).get(0).type(), isNotEmpty(serviceMethod.parameters()));
        ServiceMethodIdentifier id = serviceMethodId(serviceClass.definition().type().getSimpleName(), serviceMethod.name());
        ServiceMethodBuilder builder = ServiceMethod.builder()
                .id(id)
                .outputType(serviceMethod.returnType())
                .invoker(new MetaMethodInvoker(serviceClass, serviceMethod));
        UnaryOperator<ServiceMethodBuilder> configurator = decorator.apply(new ServiceMethodConfigurator(id, configurationProvider.get())).configure();
        if (nonNull(inputType)) {
            return configurator.apply(builder.inputType(inputType)).build();
        }
        return configurator.apply(builder).build();
    }
}
