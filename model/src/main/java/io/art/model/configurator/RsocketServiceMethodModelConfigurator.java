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
import io.art.server.decorator.*;
import io.art.server.specification.ServiceMethodSpecification.*;
import lombok.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static java.util.function.Function.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@Getter(value = PACKAGE)
@RequiredArgsConstructor(access = PACKAGE)
public class RsocketServiceMethodModelConfigurator {
    private final RsocketServiceModelConfigurator serviceModelConfigurator;
    private final String id;
    private Function<ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> decorator = identity();

    public RsocketServiceMethodModelConfigurator enableLogging() {
        return decorate(builder -> builder
                .inputDecorator(new ServiceLoggingDecorator(serviceMethod(serviceModelConfigurator.getId(), id), INPUT))
                .outputDecorator(new ServiceLoggingDecorator(serviceMethod(serviceModelConfigurator.getId(), id), OUTPUT)));
    }

    private RsocketServiceMethodModelConfigurator decorate(Function<ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> decorator) {
        this.decorator = decorator.andThen(decorator);
        return this;
    }

    RsocketServiceMethodModel configure() {
        return new RsocketServiceMethodModel(id, decorator);
    }
}
