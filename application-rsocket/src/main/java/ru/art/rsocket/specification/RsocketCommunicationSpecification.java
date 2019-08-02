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

package ru.art.rsocket.specification;

import ru.art.rsocket.model.RsocketCommunicationTargetConfiguration;
import ru.art.service.Specification;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.rsocket.constants.RsocketModuleConstants.RSOCKET_COMMUNICATION_SERVICE_TYPE;
import static ru.art.rsocket.module.RsocketModule.rsocketModule;
import java.util.List;

public interface RsocketCommunicationSpecification extends Specification {
    default RsocketCommunicationTargetConfiguration communicationTarget(String serviceId) {
        return rsocketModule().getCommunicationTargetConfiguration(serviceId);
    }

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(RSOCKET_COMMUNICATION_SERVICE_TYPE);
    }
}