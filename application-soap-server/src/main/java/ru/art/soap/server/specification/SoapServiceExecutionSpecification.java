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

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.art.http.server.model.HttpService;
import ru.art.http.server.specification.HttpServiceSpecification;
import ru.art.service.exception.UnknownServiceMethodException;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.core.constants.StringConstants.UNDERSCORE;
import static ru.art.entity.PrimitiveMapping.stringMapper;
import static ru.art.http.server.model.HttpService.httpService;
import static ru.art.soap.server.constans.SoapServerModuleConstants.*;
import static ru.art.soap.server.mapper.SoapMapper.soapRequestToModelMapper;
import static ru.art.soap.server.mapper.SoapMapper.soapResponseFromModelMapper;
import static ru.art.soap.server.service.SoapExecutionService.executeSoapService;
import static ru.art.soap.server.service.SoapExecutionService.getWsdl;

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