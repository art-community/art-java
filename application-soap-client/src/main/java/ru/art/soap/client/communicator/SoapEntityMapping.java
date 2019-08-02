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

package ru.art.soap.client.communicator;

import lombok.NoArgsConstructor;
import ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper.XmlEntityToModelMapper;
import static lombok.AccessLevel.PACKAGE;
import static ru.art.core.caster.Caster.cast;
import static ru.art.soap.client.communicator.SoapEnvelopWrappingManager.unwrapFromSoapEnvelope;
import static ru.art.soap.client.communicator.SoapEnvelopWrappingManager.wrapToSoapEnvelop;

@NoArgsConstructor(access = PACKAGE)
class SoapEntityMapping {
    static <T> XmlEntityToModelMapper<T> soapResponseToModel(SoapCommunicationConfiguration configuration) {
        return entity -> cast(configuration.getResponseMapper().map(unwrapFromSoapEnvelope(entity)));
    }

    static <T> XmlEntityFromModelMapper<T> soapRequestFromModel(SoapCommunicationConfiguration configuration) {
        return model -> wrapToSoapEnvelop(configuration.getRequestMapper().map(cast(model)), configuration);
    }
}