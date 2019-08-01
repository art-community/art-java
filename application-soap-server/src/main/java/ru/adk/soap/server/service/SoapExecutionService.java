package ru.adk.soap.server.service;

import ru.adk.core.factory.CollectionsFactory.MapBuilder;
import ru.adk.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.adk.soap.server.model.SoapRequest;
import ru.adk.soap.server.model.SoapResponse;
import ru.adk.soap.server.model.SoapService;
import ru.adk.soap.server.model.SoapService.SoapOperation;
import ru.adk.soap.server.specification.SoapServiceSpecification;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import static ru.adk.http.server.service.HttpWebResourceService.getStringResource;
import static ru.adk.soap.server.constans.SoapServerModuleConstants.SOAP_SERVICE_URL;
import static ru.adk.soap.server.module.SoapServerModule.soapServerModule;
import static ru.adk.soap.server.normalizer.WsdlPathNormalizer.normalizeUrlPath;
import java.util.Map;

public interface SoapExecutionService {
    static <RequestType, ResponseType> ResponseType executeSoapService(SoapServiceSpecification soapServiceSpecification, RequestType soapRequest) {
        SoapService soapService = soapServiceSpecification.getSoapService();
        Map<String, SoapOperation> operationServiceSpecifications = soapService.getSoapOperations();
        SoapRequest request = cast(soapRequest);
        SoapOperation soapOperation = operationServiceSpecifications.get(request.getOperationId());
        Object requestObject = soapOperation.getRequestMapper().map(request.getEntity());
        Object responseObject;
        Map<Class<? extends Exception>, XmlEntityFromModelMapper<?>> errorsMap = soapOperation.getFaultMapping();
        try {
            responseObject = soapServiceSpecification.executeMethod(soapOperation.getMethodId(), requestObject);
            return cast(SoapResponse.builder().xmlEntity(soapOperation.getResponseMapper().map(cast(responseObject))).build());
        } catch (Exception exception) {
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
