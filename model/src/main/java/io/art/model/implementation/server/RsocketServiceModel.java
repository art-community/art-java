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

package io.art.model.implementation.server;

import io.art.core.collection.*;
import io.art.model.constants.ModelConstants.*;
import io.art.server.specification.ServiceMethodSpecification.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.model.constants.ModelConstants.ConfiguratorScope.*;
import java.util.function.*;

@Getter
@Builder
public class RsocketServiceModel implements ServiceModel {
    private final Class<?> serviceClass;
    private final String id;
    private final ConfiguratorScope scope;
    private final BiFunction<String, ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> serviceDecorator;
    private final ImmutableMap<String, RsocketServiceMethodModel> methods;
}
