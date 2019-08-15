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

import lombok.experimental.UtilityClass;
import ru.art.core.factory.CollectionsFactory.MapBuilder;
import ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.art.service.constants.RequestValidationPolicy;
import ru.art.service.exception.ServiceExecutionException;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceRequest;
import ru.art.service.model.ServiceResponse;
import ru.art.soap.server.exception.SoapServerException;
import ru.art.soap.server.model.SoapRequest;
import ru.art.soap.server.model.SoapResponse;
import ru.art.soap.server.model.SoapService;
import ru.art.soap.server.model.SoapService.SoapOperation;
import ru.art.soap.server.specification.SoapServiceSpecification;
import static java.util.Objects.nonNull;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.http.server.service.HttpWebResourceService.getStringResource;
import static ru.art.service.ServiceController.executeServiceMethodUnchecked;
import static ru.art.soap.server.constans.SoapServerModuleConstants.ResponseFaultConstants.*;
import static ru.art.soap.server.constans.SoapServerModuleConstants.SOAP_SERVICE_URL;
import static ru.art.soap.server.module.SoapServerModule.soapServerModule;
import static ru.art.soap.server.normalizer.WsdlPathNormalizer.normalizeUrlPath;
import java.util.Map;

@UtilityClass
public class SoapExecutionService {
    public static <RequestType, ResponseType> ResponseType executeSoapService(SoapServiceSpecification soapServiceSpecification, RequestType soapRequest) {
        SoapService soapService = soapServiceSpecification.getSoapService();
        Map<String, SoapOperation> operationServiceSpecifications = soapService.getSoapOperations();
        SoapRequest request = cast(soapRequest);
        SoapOperation soapOperation = operationServiceSpecifications.get(request.getOperationId());
        Object requestObject = soapOperation.requestMapper().map(request.getEntity());
        Object responseObject;
        Map<Class<? extends Throwable>, XmlEntityFromModelMapper<?>> faultMapping = soapOperation.faultMapping();
        try {
            String serviceId = soapServiceSpecification.getServiceId();
            String methodId = soapOperation.methodId();
            RequestValidationPolicy validationPolicy = soapOperation.validationPolicy();
            ServiceMethodCommand serviceMethodCommand = new ServiceMethodCommand(serviceId, methodId);
            ServiceRequest<?> serviceRequest = new ServiceRequest<>(serviceMethodCommand, validationPolicy, requestObject);
            ServiceResponse<?> serviceResponse = executeServiceMethodUnchecked(serviceRequest);
            ServiceExecutionException exception;
            if (nonNull(exception = serviceResponse.getServiceException())) {
                XmlEntityFromModelMapper<?> faultMapper;
                if (isNotEmpty(faultMapper = faultMapping.get(((Throwable) exception).getClass()))) {
                    return cast(SoapResponse.builder().xmlEntity(faultMapper.map(cast((Throwable) exception))).build());
                }
                if (isNotEmpty(faultMapper = soapService.getDefaultFaultMapper())) {
                    return cast(SoapResponse.builder().xmlEntity(cast(faultMapper.map(cast(soapService.getDefaultFaultResponse())))).build());
                }
                faultMapper = soapServerModule().getDefaultFaultMapper();
                return cast(SoapResponse.builder().xmlEntity(faultMapper.map(cast(soapServerModule().getDefaultFaultResponse()))).build());
            }
            responseObject = serviceResponse.getResponseData();
            return cast(SoapResponse.builder().xmlEntity(soapOperation.responseMapper().map(cast(responseObject))).build());
        } catch (Throwable exception) {
            XmlEntityFromModelMapper<?> faultMapper;
            if (isNotEmpty(faultMapper = faultMapping.get(exception.getClass()))) {
                return cast(SoapResponse.builder().xmlEntity(faultMapper.map(cast(exception))).build());
            }
            if (isNotEmpty(faultMapper = soapService.getDefaultFaultMapper())) {
                return cast(SoapResponse.builder().xmlEntity(cast(faultMapper.map(cast(soapService.getDefaultFaultResponse())))).build());
            }
            faultMapper = soapServerModule().getDefaultFaultMapper();
            return cast(SoapResponse.builder().xmlEntity(faultMapper.map(cast(soapServerModule().getDefaultFaultResponse()))).build());
        }
    }

    public static String getWsdl(SoapServiceSpecification soapServiceSpecification) {
        SoapService soapService = soapServiceSpecification.getSoapService();
        Map<String, SoapOperation> operationServiceSpecifications = soapService.getSoapOperations();
        String wsdlResourcePath = soapServiceSpecification.getSoapService().getWsdlResourcePath();
        String wsdlServiceUrl = soapServiceSpecification.getSoapService().getWsdlServiceUrl();
        if (isEmpty(wsdlResourcePath)) {
            throw new SoapServerException(WSDL_RESOURCE_PATH_IS_EMPTY);
        }
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
