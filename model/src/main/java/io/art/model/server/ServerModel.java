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

import io.art.server.model.*;
import io.art.server.specification.*;
import lombok.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.server.model.ServiceMethodIdentifier.*;
import java.util.*;
import java.util.function.*;

@Getter
public class ServerModel {
    private final Map<ServiceMethodIdentifier, ServiceMethodSpecification> rsocketMethodsModel = mapOf();

    public ServerModel rsocket(Class<?> serviceClass) {
        return rsocket(serviceClass, UnaryOperator.identity());
    }

    public ServerModel rsocket(Class<?> serviceClass, UnaryOperator<ServiceMethodSpecification> configurator) {
        //Stub for generator
        return this;
    }

    public ServerModel rsocket(Class<?> serviceClass, String methodId) {
        return rsocket(serviceClass, methodId, UnaryOperator.identity());
    }

    public ServerModel rsocket(Class<?> serviceClass, String methodId, UnaryOperator<ServiceMethodSpecification> configurator) {
        //Stub for generator
        return this;
    }

    public ServerModel rsocket(String serviceId, String methodId) {
        return rsocket(serviceId, methodId, UnaryOperator.identity());
    }

    public ServerModel rsocket(String serviceId, String methodId, UnaryOperator<ServiceMethodSpecification> configurator) {
        //Stub for generator
        return this;
    }

    public ServerModel rsocket(ServiceMethodSpecification specification) {
        rsocketMethodsModel.put(serviceMethod(specification.getServiceId(), specification.getMethodId()), specification);
        return this;
    }
}
