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

package io.art.core.model;

import lombok.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.constants.TransportConstants.COMMUNICATOR_CLASS_SUFFIX;
import static io.art.core.constants.TransportConstants.SERVICE_CLASS_SUFFIX;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

@Value
public class CommunicatorActionIdentifier {
    private static final Map<String, CommunicatorActionIdentifier> CACHE = map();

    String communicatorId;
    String actionId;

    public static CommunicatorActionIdentifier communicatorActionId(String communicatorId, String actionId) {
        return putIfAbsent(CACHE, EMPTY_STRING + communicatorId + actionId, () -> new CommunicatorActionIdentifier(communicatorId, actionId));
    }

    public static String communicatorId(Class<?> serviceClass) {
        int suffixIndex = serviceClass.getSimpleName().toLowerCase().lastIndexOf(COMMUNICATOR_CLASS_SUFFIX);
        if (suffixIndex == -1) {
            return serviceClass.getSimpleName();
        }
        return serviceClass.getSimpleName().substring(0, suffixIndex);
    }

}
