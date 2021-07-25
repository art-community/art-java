/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.server.registry;

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.server.method.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static java.util.Optional.*;
import java.util.*;

public class ServiceMethodRegistry {
    private final Map<ServiceMethodIdentifier, ServiceMethod> methods = map();

    public Optional<ServiceMethod> findMethodById(ServiceMethodIdentifier id) {
        return ofNullable(methods.get(id));
    }

    public ImmutableSet<ServiceMethodIdentifier> identifiers() {
        return immutableSetOf(methods.keySet());
    }

    public ServiceMethodRegistry register(ServiceMethodIdentifier id, ServiceMethod method) {
        methods.put(id, method);
        return this;
    }

    public ImmutableMap<ServiceMethodIdentifier, ServiceMethod> getMethods() {
        return immutableMapOf(methods);
    }
}
