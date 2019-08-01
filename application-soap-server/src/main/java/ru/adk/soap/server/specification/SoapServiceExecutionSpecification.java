package ru.adk.soap.server.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.adk.http.server.model.HttpService;
import ru.adk.http.server.specification.HttpServiceSpecification;
import ru.adk.service.exception.UnknownServiceMethodException;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import static ru.adk.core.constants.StringConstants.UNDERSCORE;
import static ru.adk.entity.PrimitiveMapping.stringMapper;
import static ru.adk.http.server.model.HttpService.httpService;
import static ru.adk.soap.server.constans.SoapServerModuleConstants.*;
import static ru.adk.soap.server.mapper.SoapMapper.soapRequestToModelMapper;
import static ru.adk.soap.server.mapper.SoapMapper.soapResponseFromModelMapper;
import static ru.adk.soap.server.service.SoapExecutionService.executeSoapService;
import static ru.adk.soap.server.service.SoapExecutionService.getWsdl;

@Getter
@AllArgsConstructor
public class SoapServiceExecutionSpecification implements HttpServiceSpecification {
    private final SoapServiceSpecification soapServiceSpecification;
    @Getter(lazy = true)
    private final String serviceId = soapServiceSpecification.getServiceId() + UNDERSCORE + SOAP_SERVICE_TYPE;
    @Getter(lazy = true)
    private final HttpService httpService = httpService()

            .post(EXECUTE_SOAP_SERVICE)
            .consumes(soapServiceSpecification.getSoapService().getConsumes().toHttpMimeToContentTypeMapper())
            .fromBody()
            .requestMapper(soapRequestToModelMapper)
            .ignoreRequestAcceptType()
            .produces(soapServiceSpecification.getSoapService().getProduces().toHttpMimeToContentTypeMapper())
            .responseMapper(soapResponseFromModelMapper)
            .listen(soapServiceSpecification.getSoapService().getListeningPath())

            .get(GET_SERVICE_WSDL)
            .responseMapper(stringMapper.getFromModel())
            .listen(soapServiceSpecification.getSoapService().getListeningPath())

            .serve(EMPTY_STRING);

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case EXECUTE_SOAP_SERVICE:
                return cast(executeSoapService(soapServiceSpecification, cast(request)));
            case GET_SERVICE_WSDL:
                return cast(getWsdl(soapServiceSpecification));
        }
        throw new UnknownServiceMethodException(getServiceId(), methodId);
    }
}