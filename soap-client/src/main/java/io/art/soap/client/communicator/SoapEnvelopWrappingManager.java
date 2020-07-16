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

package io.art.soap.client.communicator;

import io.art.entity.immutable.*;
import lombok.*;
import io.art.soap.client.exception.*;
import static lombok.AccessLevel.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.entity.immutable.XmlEntity.*;
import static io.art.soap.client.constants.SoapClientModuleConstants.*;
import static io.art.soap.client.constants.SoapClientModuleConstants.OperationIdSource.REQUEST;
import static io.art.soap.client.constants.SoapClientModuleExceptionMessages.*;
import java.util.*;

@NoArgsConstructor(access = PACKAGE)
class SoapEnvelopWrappingManager {
    static XmlEntity wrapToSoapEnvelop(XmlEntity xmlEntity, SoapCommunicationConfiguration proxyConfig) {
        if (proxyConfig.getOperationIdSource() == REQUEST) {
            return xmlEntityBuilder()
                    .tag(SOAP_ENVELOPE_TAG)
                    .prefix(proxyConfig.getEnvelopePrefix())
                    .namespace(proxyConfig.getEnvelopeNamespace())
                    .namespaceField(proxyConfig.getEnvelopePrefix(), proxyConfig.getEnvelopeNamespace())
                    .child(xmlEntityBuilder()
                            .tag(SOAP_BODY_TAG)
                            .prefix(proxyConfig.getBodyPrefix())
                            .namespace(proxyConfig.getBodyNamespace())
                            .namespaceField(proxyConfig.getBodyPrefix(), proxyConfig.getBodyNamespace())
                            .child(xmlEntity)
                            .create())
                    .create();
        }

        String operationPrefix = proxyConfig.getOperationPrefix();
        String operationNamespace = proxyConfig.getOperationNamespace();
        XmlEntity operation = xmlEntityBuilder()
                .tag(proxyConfig.getOperationId())
                .prefix(operationPrefix)
                .namespace(operationNamespace)
                .namespaceField(operationPrefix, operationNamespace)
                .child(xmlEntity)
                .create();
        XmlEntity body = xmlEntityBuilder()
                .tag(SOAP_BODY_TAG)
                .prefix(proxyConfig.getBodyPrefix())
                .namespace(proxyConfig.getBodyNamespace())
                .namespaceField(proxyConfig.getBodyPrefix(), proxyConfig.getBodyNamespace())
                .child(operation)
                .create();
        return xmlEntityBuilder()
                .tag(SOAP_ENVELOPE_TAG)
                .prefix(proxyConfig.getEnvelopePrefix())
                .namespace(proxyConfig.getEnvelopeNamespace())
                .namespaceField(proxyConfig.getEnvelopePrefix(), proxyConfig.getEnvelopeNamespace())
                .child(body)
                .create();
    }

    static XmlEntity unwrapFromSoapEnvelope(XmlEntity xmlEntity) {
        if (isEmpty(xmlEntity) || !SOAP_ENVELOPE_TAG.equalsIgnoreCase(xmlEntity.getTag())) {
            throw new SoapClientModuleException(SOAP_RESPONSE_HAS_NOT_ENVELOPE);
        }
        XmlEntity bodyEntity;
        if (isEmpty(xmlEntity.getChildren()) || isEmpty(bodyEntity = xmlEntity.find(SOAP_BODY_TAG))) {
            throw new SoapClientModuleException(SOAP_RESPONSE_HAS_NOT_BODY);
        }
        List<XmlEntity> bodyChildren = bodyEntity.getChildren();
        XmlEntity requestEntity;
        if (isEmpty(bodyChildren) || isEmpty(requestEntity = bodyChildren.get(0))) {
            return xmlEntityBuilder().create();
        }
        return requestEntity;
    }
}
