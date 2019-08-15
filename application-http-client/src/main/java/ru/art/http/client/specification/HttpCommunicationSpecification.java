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

package ru.art.http.client.specification;

import ru.art.http.client.model.HttpCommunicationTargetConfiguration;
import ru.art.service.Specification;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.http.client.constants.HttpClientModuleConstants.HTTP_COMMUNICATION_SERVICE_TYPE;
import static ru.art.http.client.module.HttpClientModule.httpClientModule;
import static ru.art.http.constants.HttpCommonConstants.HTTP_SCHEME;
import java.util.List;

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
