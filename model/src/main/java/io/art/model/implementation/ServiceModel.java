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

package io.art.model.implementation;

import io.art.core.collection.*;
import io.art.model.constants.ModelConstants.*;
import io.art.server.specification.ServiceMethodSpecification.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import java.util.function.*;

@Getter
@Builder
public class ServiceModel {
    private final Class<?> serviceClass;
    private final String id;
    private final boolean includeAllMethods;
    private final Protocol protocol;
    private final BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> anyMethodDecorator;
    private final ImmutableMap<String, ServiceMethodModel> concreteMethods;

    public ServiceMethodSpecificationBuilder implement(String id, ServiceMethodSpecificationBuilder current) {
        if (includeAllMethods) {
            return let(anyMethodDecorator, decorator -> decorator.apply(id, current));
        }
        return let(concreteMethods.get(id), methodModel -> methodModel.implement(current));
    }
}
