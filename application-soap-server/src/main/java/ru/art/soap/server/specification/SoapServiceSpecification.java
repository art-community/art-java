/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.soap.server.specification;

import ru.art.service.Specification;
import ru.art.soap.server.model.SoapService;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.soap.server.constans.SoapServerModuleConstants.SOAP_SERVICE_TYPE;
import java.util.List;

public interface SoapServiceSpecification extends Specification {
    SoapService getSoapService();

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(SOAP_SERVICE_TYPE);
    }
}
