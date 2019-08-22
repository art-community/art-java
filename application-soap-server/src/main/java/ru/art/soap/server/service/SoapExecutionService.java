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

package ru.art.soap.server.service;

import lombok.experimental.*;
import ru.art.core.factory.CollectionsFactory.*;
import ru.art.entity.mapper.ValueFromModelMapper.*;
import ru.art.service.constants.*;
import ru.art.service.exception.*;
import ru.art.service.model.*;
import ru.art.soap.server.exception.*;
import ru.art.soap.server.model.*;
import ru.art.soap.server.model.SoapService.*;
import ru.art.soap.server.specification.*;
import java.util.*;

import static java.util.Objects.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.http.server.service.HttpResourceService.*;
import static ru.art.service.ServiceController.*;
import static ru.art.soap.server.constans.SoapServerModuleConstants.ResponseFaultConstants.*;
import static ru.art.soap.server.constans.SoapServerModuleConstants.*;
import static ru.art.soap.server.module.SoapServerModule.*;
import static ru.art.soap.server.normalizer.WsdlPathNormalizer.*;

@UtilityClass
public class SoapExecutionService {
    public static SoapResponse executeSoapService(SoapServiceSpecification soapServiceSpecification, SoapRequest soapRequest) {
        SoapService soapService = soapServiceSpecification.getSoapService();
        Map<String, SoapOperation> operationServiceSpecifications = soapService.getSoapOperations();
        SoapOperation soapOperation = operationServiceSpecifications.get(soapRequest.getOperationId());
        Object requestObject = soapOperation.requestMapper().map(soapRequest.getEntity());
        Object responseObject;
        Map<Class<? extends Throwable>, XmlEntityFromModelMapper<?>> faultMapping = soapOperation.faultMapping();
        try {
            String serviceId = soapServiceSpecification.getServiceId();
            String methodId = soapOperation.methodId();
            RequestValidationPolicy validationPolicy = soapOperation.validationPolicy();
            ServiceMethodCommand command = new ServiceMethodCommand(serviceId, methodId);
            ServiceRequest<?> serviceRequest = new ServiceRequest<>(command, validationPolicy, requestObject);
            ServiceResponse<?> serviceResponse = executeServiceMethodUnchecked(serviceRequest);
            ServiceExecutionException exception;
            if (nonNull(exception = serviceResponse.getServiceException())) {
                XmlEntityFromModelMapper<?> faultMapper;
                if (isNotEmpty(faultMapper = faultMapping.get(((Throwable) exception).getClass()))) {
                    return SoapResponse.builder().xmlEntity(faultMapper.map(cast((Throwable) exception))).build();
                }
                if (isNotEmpty(faultMapper = soapService.getDefaultFaultMapper())) {
                    return SoapResponse.builder().xmlEntity(cast(faultMapper.map(cast(soapService.getDefaultFaultResponse())))).build();
                }
                faultMapper = soapServerModule().getDefaultFaultMapper();
                return SoapResponse.builder().xmlEntity(faultMapper.map(cast(soapServerModule().getDefaultFaultResponse()))).build();
            }
            responseObject = serviceResponse.getResponseData();
            return SoapResponse.builder().xmlEntity(soapOperation.responseMapper().map(cast(responseObject))).build();
        } catch (Throwable exception) {
            XmlEntityFromModelMapper<?> faultMapper;
            if (isNotEmpty(faultMapper = faultMapping.get(exception.getClass()))) {
                return SoapResponse.builder().xmlEntity(faultMapper.map(cast(exception))).build();
            }
            if (isNotEmpty(faultMapper = soapService.getDefaultFaultMapper())) {
                return SoapResponse.builder().xmlEntity(cast(faultMapper.map(cast(soapService.getDefaultFaultResponse())))).build();
            }
            faultMapper = soapServerModule().getDefaultFaultMapper();
            return SoapResponse.builder().xmlEntity(faultMapper.map(cast(soapServerModule().getDefaultFaultResponse()))).build();
        }
    }

    public static String getWsdl(SoapServiceSpecification soapServiceSpecification) {
        SoapService soapService = soapServiceSpecification.getSoapService();
        String wsdlResourcePath = soapService.getWsdlResourcePath();
        if (isEmpty(wsdlResourcePath)) {
            throw new SoapServerException(WSDL_RESOURCE_PATH_IS_EMPTY);
        }
        String wsdlServiceUrl = soapService.getWsdlServiceUrl();
        if (isEmpty(wsdlServiceUrl)) {
            throw new SoapServerException(WSDL_SERVICE_URL_IS_EMPTY);
        }
        String normalizeWsdlPath = normalizeUrlPath(wsdlResourcePath);
        String normalizedServiceUrl = normalizeUrlPath(wsdlServiceUrl);
        MapBuilder<String, String> templateMapping = mapOf(SOAP_SERVICE_URL, normalizedServiceUrl);
        String wsdl = getStringResource(normalizeWsdlPath, templateMapping);
        if (isEmpty(wsdl)) {
            throw new SoapServerException(WSDL_IS_EMPTY);
        }
        return wsdl;
    }
}
