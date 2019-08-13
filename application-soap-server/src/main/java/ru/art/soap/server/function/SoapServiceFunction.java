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

package ru.art.soap.server.function;

import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper.XmlEntityToModelMapper;
import ru.art.service.constants.RequestValidationPolicy;
import ru.art.soap.content.mapper.SoapMimeToContentTypeMapper;
import ru.art.soap.server.specification.SoapFunctionalServiceSpecification;
import static ru.art.core.caster.Caster.cast;
import static ru.art.service.ServiceModule.*;
import static ru.art.soap.server.constans.SoapServerModuleConstants.EXECUTE_SOAP_FUNCTION;
import static ru.art.soap.server.model.SoapService.*;
import static ru.art.soap.server.model.SoapService.SoapOperation.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SoapServiceFunction {
    private final SoapServiceBuilder soapService;
    private final String operationId;
    private final SoapOperation soapOperation;
    private final String path;

    private SoapServiceFunction(String path, String operationId) {
        this.path = path;
        soapService = soapService();
        this.operationId = operationId;
        soapOperation = soapOperation().methodId(EXECUTE_SOAP_FUNCTION);
    }

    public SoapServiceFunction wsdlServiceUrl(String serviceUrl) {
        soapService.wsdlServiceUrl(serviceUrl);
        return this;
    }

    public SoapServiceFunction wsdlResourcePath(String wsdlResourcePath) {
        soapService.wsdlResourcePath(wsdlResourcePath);
        return this;
    }

    public SoapServiceFunction defaultFaultResponse(String defaultFaultResponse) {
        soapService.defaultFaultResponse(defaultFaultResponse);
        return this;
    }

    public SoapServiceFunction ignoreRequestAcceptType(boolean ignoreRequestAcceptType) {
        soapService.ignoreRequestAcceptType(ignoreRequestAcceptType);
        return this;
    }

    public SoapServiceFunction ignoreRequestContentType(boolean ignoreRequestContentType) {
        soapService.ignoreRequestContentType(ignoreRequestContentType);
        return this;
    }

    public <FaultType> SoapServiceFunction defaultFaultMapper(XmlEntityFromModelMapper<FaultType> mapper) {
        soapService.defaultFaultMapper(mapper);
        return this;
    }

    public SoapServiceFunction consumes(SoapMimeToContentTypeMapper consumes) {
        soapService.consumes(consumes);
        return this;
    }

    public SoapServiceFunction produces(SoapMimeToContentTypeMapper produces) {
        soapService.produces(produces);
        return this;
    }

    public SoapServiceFunction validationPolicy(RequestValidationPolicy policy) {
        soapOperation.validationPolicy(policy);
        return this;
    }

    public <RequestType> SoapServiceFunction requestMapper(XmlEntityToModelMapper<RequestType> requestMapper) {
        soapOperation.requestMapper(requestMapper);
        return this;
    }

    public <ResponseType> SoapServiceFunction responseMapper(XmlEntityFromModelMapper<ResponseType> responseMapper) {
        soapOperation.responseMapper(responseMapper);
        return this;
    }

    public <ExceptionType extends Throwable> SoapServiceFunction addFaultMapping(Class<ExceptionType> exceptionClass, XmlEntityFromModelMapper<ExceptionType> mapper) {
        soapOperation.faultMapper(exceptionClass, mapper);
        return this;
    }

    public SoapServiceFunction addRequestValueInterceptor(ValueInterceptor interceptor) {
        soapOperation.addRequestValueInterceptor(interceptor);
        return this;
    }

    public SoapServiceFunction addResponseValueInterceptor(ValueInterceptor interceptor) {
        soapOperation.addResponseValueInterceptor(interceptor);
        return this;
    }

    public <RequestType, ResponseType> void handle(Function<RequestType, ResponseType> function) {
        serviceModule()
                .getServiceRegistry()
                .registerService(new SoapFunctionalServiceSpecification(soapService
                        .operation(operationId, soapOperation)
                        .serve(path), function));
    }

    public <RequestType> void consume(Consumer<RequestType> consumer) {
        handle(request -> {
            consumer.accept(cast(request));
            return null;
        });
    }

    public <ResponseType> void produce(Supplier<ResponseType> producer) {
        handle(request -> producer.get());
    }

    public static SoapServiceFunction soap(String path, String operationId) {
        return new SoapServiceFunction(path, operationId);
    }
}
