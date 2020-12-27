/*
 * ART
 *
 * Copyright 2020 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.model.configurator;

import io.art.model.implementation.*;
import io.art.server.decorator.*;
import io.art.server.specification.ServiceMethodSpecification.*;
import lombok.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.model.constants.ModelConstants.*;
import static io.art.server.model.ServiceMethodIdentifier.*;
import static java.util.function.UnaryOperator.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@Getter(value = PACKAGE)
@RequiredArgsConstructor(access = PACKAGE)
public class ServiceModelConfigurator<T> {
    private final Protocol protocol;
    private Class<?> serviceClass;
    private String id;
    private boolean includeAllMethods;
    private final Map<String, ServiceMethodModelConfigurator> concreteMethods = map();
    private BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> anyMethod = (id, builder) -> builder;

    public ServiceModelConfigurator<T> to(Class<?> service) {
        return to(service, identity());
    }

    public ServiceModelConfigurator<T> to(Class<?> service, UnaryOperator<ServiceModelConfigurator<T>> operator) {
        this.id = service.getSimpleName();
        this.serviceClass = service;
        this.includeAllMethods = true;
        return operator.apply(this);
    }

    public ServiceModelConfigurator<T> to(Class<?> service, String method) {
        return to(service, method, identity());
    }

    public ServiceModelConfigurator<T> to(Class<?> service, String method, UnaryOperator<ServiceMethodModelConfigurator> operator) {
        this.id = service.getSimpleName();
        this.serviceClass = service;
        operator.apply(putIfAbsent(this.concreteMethods, method, () -> new ServiceMethodModelConfigurator(this, method)));
        return this;
    }

    public ServiceModelConfigurator<T> enableLogging() {
        return decorateAny((method, builder) -> builder
                .inputDecorator(new ServiceLoggingDecorator(serviceMethod(serviceClass.getSimpleName(), method), INPUT))
                .outputDecorator(new ServiceLoggingDecorator(serviceMethod(serviceClass.getSimpleName(), method), OUTPUT)));
    }

    public ServiceModelConfigurator<T> enableLogging(String id) {
        return decorateConcrete(id, (method, builder) -> builder.enableLogging());
    }

    private ServiceModelConfigurator<T> decorateAny(BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> decorator) {
        BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> current = this.anyMethod;
        anyMethod = (method, builder) -> {
            builder = current.apply(method, builder);
            return decorator.apply(method, builder);
        };
        return this;
    }

    private ServiceModelConfigurator<T> decorateConcrete(String method, BiConsumer<String, ServiceMethodModelConfigurator> decorator) {
        decorator.accept(method, putIfAbsent(this.concreteMethods, method, () -> new ServiceMethodModelConfigurator(this, method)));
        return this;
    }

    ServiceModel configure() {
        return ServiceModel.<T>builder()
                .anyMethodDecorator(anyMethod)
                .concreteMethods(concreteMethods.entrySet().stream().collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().configure())))
                .includeAllMethods(includeAllMethods)
                .id(id)
                .serviceClass(serviceClass)
                .protocol(protocol)
                .build();
    }
}
