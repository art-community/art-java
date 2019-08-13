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

package ru.art.soap.server.service;

import ru.art.core.factory.CollectionsFactory.MapBuilder;
import ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.art.soap.server.model.SoapRequest;
import ru.art.soap.server.model.SoapResponse;
import ru.art.soap.server.model.SoapService;
import ru.art.soap.server.model.SoapService.SoapOperation;
import ru.art.soap.server.specification.SoapServiceSpecification;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.http.server.service.HttpWebResourceService.getStringResource;
import static ru.art.soap.server.constans.SoapServerModuleConstants.SOAP_SERVICE_URL;
import static ru.art.soap.server.module.SoapServerModule.soapServerModule;
import static ru.art.soap.server.normalizer.WsdlPathNormalizer.normalizeUrlPath;
import java.util.Map;

public interface SoapExecutionService {
    static <RequestType, ResponseType> ResponseType executeSoapService(SoapServiceSpecification soapServiceSpecification, RequestType soapRequest) {
        SoapService soapService = soapServiceSpecification.getSoapService();
        Map<String, SoapOperation> operationServiceSpecifications = soapService.getSoapOperations();
        SoapRequest request = cast(soapRequest);
        SoapOperation soapOperation = operationServiceSpecifications.get(request.getOperationId());
        Object requestObject = soapOperation.getRequestMapper().map(request.getEntity());
        Object responseObject;
        Map<Class<? extends Throwable>, XmlEntityFromModelMapper<?>> errorsMap = soapOperation.getFaultMapping();
        try {
            responseObject = soapServiceSpecification.executeMethod(soapOperation.getMethodId(), requestObject);
            return cast(SoapResponse.builder().xmlEntity(soapOperation.getResponseMapper().map(cast(responseObject))).build());
        } catch (Throwable exception) {
            XmlEntityFromModelMapper<?> faultMapper;
            if (isNotEmpty(faultMapper = errorsMap.get(exception.getClass()))) {
                return cast(SoapResponse.builder().xmlEntity(faultMapper.map(cast(exception))).build());
            }
            if (isNotEmpty(faultMapper = soapService.getDefaultFaultMapper())) {
                return cast(SoapResponse.builder().xmlEntity(cast(faultMapper.map(cast(soapService.getDefaultFaultResponse())))).build());
            }
            faultMapper = soapServerModule().getDefaultFaultMapper();
            return cast(SoapResponse.builder().xmlEntity(faultMapper.map(cast(soapServerModule().getDefaultFaultResponse()))).build());
        }
    }

    static String getWsdl(SoapServiceSpecification soapServiceSpecification) {
        String normalizeWsdlPath = normalizeUrlPath(soapServiceSpecification.getSoapService().getWsdlResourcePath());
        String normalizedServiceUrl = normalizeUrlPath(soapServiceSpecification.getSoapService().getServiceUrl());
        MapBuilder<String, String> templateMapping = mapOf(SOAP_SERVICE_URL, normalizedServiceUrl);
        return getStringResource(normalizeWsdlPath, templateMapping);
    }
}
