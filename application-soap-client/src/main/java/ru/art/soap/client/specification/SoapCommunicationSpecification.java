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

package ru.art.soap.client.specification;

import ru.art.http.client.specification.HttpCommunicationSpecification;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.soap.client.constants.SoapClientModuleConstants.SOAP_COMMUNICATION_SERVICE_TYPE;
import java.util.List;

public interface SoapCommunicationSpecification extends HttpCommunicationSpecification {
    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(SOAP_COMMUNICATION_SERVICE_TYPE);
    }
}
