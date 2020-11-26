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

import io.art.server.specification.ServiceMethodSpecification.*;
import lombok.*;
import static io.art.server.specification.ServiceSpecification.*;
import java.util.function.*;

@Getter
@RequiredArgsConstructor
public class ServiceModelCustomizer<T> {
    private final ServiceModel<T> serviceModel;
    private UnaryOperator<T> configurator;
    private UnaryOperator<ServiceSpecificationBuilder> serviceCustomizer;
    private UnaryOperator<ServiceMethodSpecificationBuilder> methodCustomizer;

    public ServiceModelCustomizer<T> configure(UnaryOperator<T> configurator) {
        this.configurator = configurator;
        return this;
    }

    public ServiceModelCustomizer<T> customizeService(UnaryOperator<ServiceSpecificationBuilder> customizer) {
        this.serviceCustomizer = customizer;
        return this;
    }

    public ServiceModelCustomizer<T> customizeMethod(UnaryOperator<ServiceMethodSpecificationBuilder> customizer) {
        this.methodCustomizer = customizer;
        return this;
    }
}
