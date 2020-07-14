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

package io.art.soap.server.service;

import lombok.experimental.*;
import io.art.entity.mapper.ValueFromModelMapper.*;
import io.art.service.constants.*;
import io.art.service.exception.*;
import io.art.service.model.*;
import io.art.soap.server.exception.*;
import io.art.soap.server.model.*;
import io.art.soap.server.model.SoapService.*;
import io.art.soap.server.specification.*;
import static java.util.Objects.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.CheckerForEmptiness.*;
import static io.art.http.server.module.HttpServerModule.*;
import static io.art.http.server.service.HttpResourceService.*;
import static io.art.service.ServiceController.*;
import static io.art.soap.server.constans.SoapServerModuleConstants.ResponseFaultConstants.*;
import static io.art.soap.server.constans.SoapServerModuleConstants.*;
import static io.art.soap.server.module.SoapServerModule.*;
import static io.art.soap.server.normalizer.WsdlPathNormalizer.*;
import java.util.*;

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
        String wsdl = getStringResource(normalizeWsdlPath, httpServerModule()
                .getResourceConfiguration()
                .toBuilder()
                .templateResourceVariable(SOAP_SERVICE_URL, normalizedServiceUrl).build());
        if (isEmpty(wsdl)) {
            throw new SoapServerException(WSDL_IS_EMPTY);
        }
        return wsdl;
    }
}
