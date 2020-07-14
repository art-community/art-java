/*
 * ART Java
 *
 * Copyright 2019 ART
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

package io.art.http.client.specification;

import io.art.http.client.model.*;
import io.art.service.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.http.client.constants.HttpClientModuleConstants.*;
import static io.art.http.client.module.HttpClientModule.*;
import static io.art.http.constants.HttpCommonConstants.*;
import java.util.*;

public interface HttpCommunicationSpecification extends Specification {
    default HttpCommunicationTargetConfiguration communicationTarget(String serviceId) {
        return httpClientModule().getCommunicationTargetConfiguration(serviceId);
    }

    default String getScheme() {
        return HTTP_SCHEME;
    }

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(HTTP_COMMUNICATION_SERVICE_TYPE);
    }
}
