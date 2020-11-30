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

package io.art.model.modeler;

import io.art.model.implementation.*;
import io.art.server.decorator.*;
import io.art.server.specification.ServiceMethodSpecification.*;
import lombok.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.server.model.ServiceMethodIdentifier.*;
import static java.util.function.Function.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@Getter(value = PACKAGE)
@RequiredArgsConstructor(access = PACKAGE)
public class ServiceMethodModeler {
    private final ServiceModeler<?> serviceModeler;
    private final String id;
    private Function<ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> decorator = identity();

    public ServiceMethodModeler enableLogging() {
        decorator = decorator.andThen(builder -> builder
                .inputDecorator(new ServiceLoggingDecorator(serviceMethod(serviceModeler.getServiceClass().getSimpleName(), id), INPUT))
                .inputDecorator(new ServiceLoggingDecorator(serviceMethod(serviceModeler.getServiceClass().getSimpleName(), id), OUTPUT)));
        return this;
    }

    ServiceMethodModel apply() {
        return new ServiceMethodModel(id, decorator);
    }
}
