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

package io.art.model.server;

import com.google.common.collect.*;
import io.art.model.constants.ModelConstants.*;
import io.art.server.model.*;
import io.art.server.specification.ServiceMethodSpecification.*;
import io.art.server.specification.ServiceSpecification.*;
import lombok.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

@RequiredArgsConstructor
public class ServiceModel<T> {
    @Getter
    private Class<?> serviceClass;
    @Getter
    private boolean allMethods;
    @Getter
    private final Protocols protocol;
    @Getter
    private UnaryOperator<ServiceModelCustomizer<T>> customizer;
    private final ImmutableSet.Builder<String> concreteMethods = ImmutableSet.builder();

    public ServiceModel<T> toClass(Class<?> service) {
        return toClass(service, identity());
    }

    public ServiceModel<T> toClass(Class<?> service, UnaryOperator<ServiceModelCustomizer<T>> customizer) {
        this.serviceClass = service;
        this.allMethods = true;
        this.customizer = customizer;
        return this;
    }


    public ServiceModel<T> toClassMethod(Class<?> service, String method) {
        return toClassMethod(service, method, identity());
    }

    public ServiceModel<T> toClassMethod(Class<?> service, String method, UnaryOperator<ServiceModelCustomizer<T>> customizer) {
        this.serviceClass = service;
        this.concreteMethods.add(method);
        this.customizer = customizer;
        return this;
    }


    public ImmutableSet<String> getConcreteMethods() {
        return concreteMethods.build();
    }
}
