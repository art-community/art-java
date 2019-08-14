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

package ru.art.soap.server.specification;

import lombok.Getter;
import ru.art.http.server.builder.HttpServiceBuilder;
import ru.art.http.server.model.HttpService;
import ru.art.http.server.specification.HttpServiceSpecification;
import ru.art.service.exception.UnknownServiceMethodException;
import ru.art.soap.server.model.SoapService;
import static java.util.Objects.isNull;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.entity.PrimitiveMapping.stringMapper;
import static ru.art.http.server.builder.HttpServiceBuilder.HttpMethodResponseBuilder;
import static ru.art.http.server.builder.HttpServiceBuilder.HttpMethodWithBodyBuilder;
import static ru.art.http.server.model.HttpService.httpService;
import static ru.art.service.ServiceModule.serviceModule;
import static ru.art.service.ServiceModuleConfiguration.ServiceRegistry;
import static ru.art.soap.server.constans.SoapServerModuleConstants.*;
import static ru.art.soap.server.mapper.SoapMapper.soapRequestToModelMapper;
import static ru.art.soap.server.mapper.SoapMapper.soapResponseFromModelMapper;
import static ru.art.soap.server.service.SoapExecutionService.executeSoapService;
import static ru.art.soap.server.service.SoapExecutionService.getWsdl;

@Getter
public class SoapServiceExecutionSpecification implements HttpServiceSpecification {
    private final SoapServiceSpecification soapServiceSpecification;
    @Getter(lazy = true)
    private final String serviceId = SOAP_EXECUTION_SERVICE_TYPE + OPENING_BRACKET + soapServiceSpecification.getServiceId() + CLOSING_BRACKET;
    @Getter(lazy = true)
    private final HttpService httpService = addExecuteSoapServiceOperation(httpService())
            .get(GET_SERVICE_WSDL)
            .responseMapper(stringMapper.getFromModel())
            .listen(soapServiceSpecification.getSoapService().getPath())

            .serve(EMPTY_STRING);

    public SoapServiceExecutionSpecification(SoapServiceSpecification soapServiceSpecification) {
        this.soapServiceSpecification = soapServiceSpecification;
        ServiceRegistry serviceRegistry = serviceModule().getServiceRegistry();
        if (isNull(serviceRegistry.getService(soapServiceSpecification.getServiceId()))) {
            serviceRegistry.registerService(soapServiceSpecification);
        }
    }


    @SuppressWarnings("all")
    private HttpServiceBuilder addExecuteSoapServiceOperation(HttpServiceBuilder builder) {
        SoapService soapService = soapServiceSpecification.getSoapService();
        HttpMethodWithBodyBuilder methodWithBodyBuilder = builder.post(EXECUTE_SOAP_SERVICE)
                .consumes(soapService.getConsumes().toHttpMimeToContentTypeMapper());
        if (soapServiceSpecification.getSoapService().isIgnoreRequestContentType()) {
            methodWithBodyBuilder = methodWithBodyBuilder.ignoreRequestContentType();
        }
        HttpMethodResponseBuilder methodResponseBuilder = methodWithBodyBuilder
                .fromBody()
                .requestMapper(soapRequestToModelMapper);
        if (soapServiceSpecification.getSoapService().isIgnoreRequestAcceptType()) {
            methodResponseBuilder = methodResponseBuilder.ignoreRequestAcceptType();
        }
        return methodResponseBuilder.produces(soapService.getProduces().toHttpMimeToContentTypeMapper())
                .responseMapper(soapResponseFromModelMapper)
                .listen(soapService.getPath());
    }

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