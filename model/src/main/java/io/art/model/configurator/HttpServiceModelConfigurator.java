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

import io.art.model.implementation.server.*;
import io.art.server.specification.ServiceMethodSpecification.*;
import lombok.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.factory.MapFactory.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@Getter(value = PACKAGE)
public class HttpServiceModelConfigurator {
    private String id;
    private final Class<?> serviceClass;
    private final Map<String, HttpServiceMethodModelConfigurator> methods = map();
    private BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> decorator = (id, builder) -> builder;

    public HttpServiceModelConfigurator(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
        this.id = serviceClass.getSimpleName();
    }

    public HttpServiceModelConfigurator id(String id) {
        this.id = id;
        return this;
    }

    public HttpServiceModelConfigurator method(String name) {
        return method(name, UnaryOperator.identity());
    }

    public HttpServiceModelConfigurator method(String name, UnaryOperator<HttpServiceMethodModelConfigurator> configurator) {
        methods.putIfAbsent(name, configurator.apply(new HttpServiceMethodModelConfigurator(name)));
        return this;
    }

    public HttpServiceModelConfigurator decorate(BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> decorator) {
        BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> current = this.decorator;
        this.decorator = (method, builder) -> {
            builder = current.apply(method, builder);
            return decorator.apply(method, builder);
        };
        return this;
    }

    HttpServiceModel configure() {
        return HttpServiceModel.builder()
                .id(id)
                .serviceClass(serviceClass)
                .decorator(decorator)
                .methods(methods
                        .entrySet()
                        .stream()
                        .collect(immutableMapCollector(entry -> entry.getValue().getId(), entry -> entry.getValue().configure())))
                .build();
    }
}
