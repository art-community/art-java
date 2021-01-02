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

import io.art.model.constants.ModelConstants.*;
import io.art.model.implementation.server.*;
import io.art.server.decorator.*;
import io.art.server.specification.ServiceMethodSpecification.*;
import lombok.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.serviceMethod;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@Getter(value = PACKAGE)
@RequiredArgsConstructor(access = PACKAGE)
public class RsocketServiceModelConfigurator {
    private final Class<?> serviceClass;
    private final String id;
    private final ConfiguratorScope scope;
    private final Map<String, RsocketServiceMethodModelConfigurator> methods = map();
    private BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> classDecorator = (id, builder) -> builder;

    public RsocketServiceModelConfigurator method(String id) {
        return method(id, UnaryOperator.identity());
    }

    public RsocketServiceModelConfigurator method(String id, UnaryOperator<RsocketServiceMethodModelConfigurator> configurator) {
        methods.putIfAbsent(id, configurator.apply(new RsocketServiceMethodModelConfigurator(this, id)));
        return this;
    }

    public RsocketServiceModelConfigurator enableLogging() {
        return decorate((method, builder) -> builder
                .inputDecorator(new ServiceLoggingDecorator(serviceMethod(serviceClass.getSimpleName(), method), INPUT))
                .outputDecorator(new ServiceLoggingDecorator(serviceMethod(serviceClass.getSimpleName(), method), OUTPUT)));
    }

    private RsocketServiceModelConfigurator decorate(BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> decorator) {
        BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> current = this.classDecorator;
        classDecorator = (method, builder) -> {
            builder = current.apply(method, builder);
            return decorator.apply(method, builder);
        };
        return this;
    }

    RsocketServiceModel configure() {
        return RsocketServiceModel.builder()
                .classDecorator(classDecorator)
                .methods(methods.entrySet().stream().collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().configure())))
                .scope(scope)
                .id(id)
                .serviceClass(serviceClass)
                .build();
    }
}
