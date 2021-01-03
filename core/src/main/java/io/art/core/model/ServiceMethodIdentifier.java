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

package io.art.core.model;

import lombok.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

@Value
public class ServiceMethodIdentifier {
    private final static Map<String, ServiceMethodIdentifier> CACHE = concurrentHashMap();

    String serviceId;
    String methodId;

    public static ServiceMethodIdentifier serviceMethod(String serviceId, String methodId) {
        return putIfAbsent(CACHE, EMPTY_STRING + serviceId + methodId, () -> new ServiceMethodIdentifier(serviceId, methodId));
    }
}
