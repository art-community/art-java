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

package io.art.soap.server.mapper;

import io.art.soap.server.model.*;
import static io.art.value.immutable.XmlEntity.*;
import static io.art.value.mapper.ValueFromModelMapper.*;
import static io.art.value.mapper.ValueToModelMapper.*;
import static io.art.soap.server.constans.SoapServerModuleConstants.*;

public interface SoapMapper {
    XmlToModelMapper<SoapRequest> soapRequestToModelMapper = SoapMapper::buildRequest;
    XmlFromModelMapper<SoapResponse> soapResponseFromModelMapper = response -> xmlEntityBuilder()
            .tag(ENVELOPE)
            .prefix(PREFIX)
            .namespace(NAMESPACE)
            .namespaceField(PREFIX, NAMESPACE)
            .child(xmlEntityBuilder()
                    .tag(HEADER)
                    .prefix(PREFIX)
                    .namespace(NAMESPACE)
                    .namespaceField(PREFIX, NAMESPACE)
                    .create())
            .child(xmlEntityBuilder()
                    .tag(BODY)
                    .prefix(PREFIX)
                    .namespace(NAMESPACE)
                    .namespaceField(PREFIX, NAMESPACE)
                    .child(response.getXmlEntity())
                    .create())
            .create();
    XmlFromModelMapper<SoapFault> soapResponseFaultMapper = response -> xmlEntityBuilder()
            .tag(FAULT)
            .prefix(PREFIX)
            .namespace(NAMESPACE)
            .namespaceField(PREFIX, NAMESPACE)
            .child(xmlEntityBuilder()
                    .tag(CODE)
                    .prefix(PREFIX)
                    .namespace(NAMESPACE)
                    .namespaceField(PREFIX, NAMESPACE)
                    .child(xmlEntityBuilder()
                            .tag(VALUE)
                            .prefix(PREFIX)
                            .namespace(NAMESPACE)
                            .namespaceField(PREFIX, NAMESPACE)
                            .value(response.getCodeValue())
                            .create())
                    .create())
            .child(xmlEntityBuilder()
                    .tag(REASON)
                    .prefix(PREFIX)
                    .namespace(NAMESPACE)
                    .namespaceField(PREFIX, NAMESPACE)
                    .child(xmlEntityBuilder()
                            .tag(TEXT)
                            .prefix(PREFIX)
                            .namespace(NAMESPACE)
                            .namespaceField(PREFIX, NAMESPACE)
                            .value(response.getReasonText())
                            .create())
                    .create())
            .create();

    static SoapRequest buildRequest(XmlEntity xmlEntity) {
        xmlEntity = xmlEntity.find(BODY).getChildren().get(0);
        return SoapRequest.builder()
                .operationId(xmlEntity.getTag())
                .entity(xmlEntity)
                .build();
    }
}
