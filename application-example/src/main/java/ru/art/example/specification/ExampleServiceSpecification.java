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

package ru.art.example.specification;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.RetryConfig;
import lombok.Getter;
import lombok.ToString;
import ru.art.example.api.model.ExampleRequest;
import ru.art.example.interceptor.ExampleServiceInterception;
import ru.art.example.interceptor.http.ExampleServiceHttpInterception;
import ru.art.grpc.server.model.GrpcService;
import ru.art.grpc.server.specification.GrpcServiceSpecification;
import ru.art.http.server.interceptor.HttpServerInterceptor;
import ru.art.http.server.model.HttpService;
import ru.art.http.server.specification.HttpServiceSpecification;
import ru.art.service.exception.UnknownServiceMethodException;
import ru.art.service.interceptor.ServiceExecutionInterceptor.ServiceRequestInterceptor;
import ru.art.service.interceptor.ServiceExecutionInterceptor.ServiceResponseInterceptor;
import ru.art.service.model.CircuitBreakerServiceConfig;
import ru.art.service.model.RateLimiterServiceConfig;
import ru.art.service.model.RetryServiceConfig;
import ru.art.service.model.ServiceExecutionConfiguration;
import ru.art.soap.server.model.SoapService;
import ru.art.soap.server.specification.SoapServiceSpecification;
import static ru.art.config.extensions.ConfigExtensions.configString;
import static ru.art.config.extensions.http.HttpConfigKeys.HTTP_SERVER_SECTION_ID;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.core.factory.CollectionsFactory.linkedListOf;
import static ru.art.example.api.constants.ExampleApiConstants.EXAMPLE_SERVICE_ID;
import static ru.art.example.api.constants.ExampleApiConstants.Methods.*;
import static ru.art.example.api.constants.ExampleApiConstants.Paths.*;
import static ru.art.example.api.constants.ExampleApiConstants.SoapConstants.EXAMPLE_OPERATION;
import static ru.art.example.api.mapping.ExampleRequestResponseMapper.ExampleRequestMapper.toExampleRequest;
import static ru.art.example.api.mapping.ExampleRequestResponseMapper.ExampleResponseMapper.fromExampleResponse;
import static ru.art.example.api.mapping.ExampleStateModelMapper.fromExampleStateModel;
import static ru.art.example.constants.ExampleAppModuleConstants.ConfigKeys.*;
import static ru.art.example.service.ExampleService.*;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVICE_TYPE;
import static ru.art.grpc.server.model.GrpcService.GrpcMethod.grpcMethod;
import static ru.art.grpc.server.model.GrpcService.grpcService;
import static ru.art.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import static ru.art.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import static ru.art.http.server.module.HttpServerModule.httpServerModule;
import static ru.art.service.ServiceModule.serviceModule;
import static ru.art.service.constants.RequestValidationPolicy.VALIDATABLE;
import static ru.art.service.constants.ServiceExecutionFeatureTarget.SERVICE;
import static ru.art.service.interceptor.ServiceExecutionInterceptor.interceptRequest;
import static ru.art.service.interceptor.ServiceExecutionInterceptor.interceptResponse;
import static ru.art.soap.server.constans.SoapServerModuleConstants.SOAP_SERVICE_TYPE;
import static ru.art.soap.server.constans.SoapServerModuleConstants.WSDL_EXTENSION;
import java.util.List;


@ToString
@Getter
public class ExampleServiceSpecification implements HttpServiceSpecification, GrpcServiceSpecification, SoapServiceSpecification {
    private final ServiceExecutionConfiguration serviceExecutionConfiguration = ServiceExecutionConfiguration.builder()
            .retryTarget(SERVICE)
            .retryConfig(RetryServiceConfig.builder().retryable(true).retryConfigBuilder(RetryConfig.custom()).build())
            .circuitBreakTarget(SERVICE)
            .circuitBreakerConfig(CircuitBreakerServiceConfig.builder().breakable(true).circuitBreakerConfigBuilder(CircuitBreakerConfig.custom()).build())
            .rateLimiterTarget(SERVICE)
            .rateLimiterConfig(RateLimiterServiceConfig.builder().limited(true).rateLimiterConfigBuilder(RateLimiterConfig.custom()).build())
            .build();

    private final String serviceId = EXAMPLE_SERVICE_ID;

    private final HttpService httpService = HttpService.httpService()
            .addRequestInterceptor(HttpServerInterceptor.intercept(new ExampleServiceHttpInterception()))

            .post(REQUEST_RESPONSE_HANDLING_EXAMPLE)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(VALIDATABLE)
            .requestMapper(toExampleRequest)
            .produces(applicationJsonUtf8())
            .responseMapper(fromExampleResponse)
            .listen(REQUEST_RESPONSE_HANDLING_EXAMPLE_PATH)

            .post(USING_CONFIGURATION_VALUES_EXAMPLE)
            .listen(USING_CONFIGURATION_VALUES_EXAMPLE_PATH)

            .post(SOAP_CLIENT_EXAMPLE)
            .listen(SOAP_CLIENT_EXAMPLE_PATH)

            .post(HTTP_CLIENT_EXAMPLE)
            .listen(HTTP_CLIENT_EXAMPLE_PATH)

            .post(GRPC_CLIENT_EXAMPLE)
            .listen(GRPC_CLIENT_EXAMPLE_PATH)

            .post(SQL_EXAMPLE)
            .listen(SQL_EXAMPLE_PATH)

            .post(ROCKS_DB_EXAMPLE)
            .listen(ROCKS_DB_EXAMPLE_PATH)

            .post(LOGGING_EXAMPLE)
            .listen(LOGGING_EXAMPLE_PATH)

            .post(JSON_READ_WRITE_EXAMPLE)
            .listen(JSON_READ_WRITE_EXAMPLE_PATH)

            .post(PROTOBUF_READ_WRITE_EXAMPLE)
            .listen(PROTOBUF_READ_WRITE_EXAMPLE_PATH)

            .post(ASYNC_TASK_EXECUTING_EXAMPLE)
            .listen(ASYNC_TASK_EXECUTING_EXAMPLE_PATH)

            .post(GET_EXAMPLE_MODULE_STATE)
            .produces(applicationJsonUtf8())
            .responseMapper(fromExampleStateModel)
            .listen(GET_EXAMPLE_MODULE_STATE_PATH)

            .serve(httpServerModule().getPath());

    private final GrpcService grpcService = grpcService()
            .method(REQUEST_RESPONSE_HANDLING_EXAMPLE, grpcMethod()
                    .requestMapper(toExampleRequest)
                    .validationPolicy(VALIDATABLE)
                    .responseMapper(fromExampleResponse))
            .method(USING_CONFIGURATION_VALUES_EXAMPLE, grpcMethod())
            .method(SOAP_CLIENT_EXAMPLE, grpcMethod())
            .method(HTTP_CLIENT_EXAMPLE, grpcMethod())
            .method(GRPC_CLIENT_EXAMPLE, grpcMethod())
            .method(SQL_EXAMPLE, grpcMethod())
            .method(ROCKS_DB_EXAMPLE, grpcMethod())
            .method(LOGGING_EXAMPLE, grpcMethod())
            .method(JSON_READ_WRITE_EXAMPLE, grpcMethod())
            .method(PROTOBUF_READ_WRITE_EXAMPLE, grpcMethod())
            .method(ASYNC_TASK_EXECUTING_EXAMPLE, grpcMethod())
            .method(GET_EXAMPLE_MODULE_STATE, grpcMethod().responseMapper(fromExampleStateModel))
            .serve();

    //todo soap specification
    private final SoapService soapService = SoapService.builder()
            .soapOperation(EXAMPLE_OPERATION, SoapService.SoapOperation.builder()
                    .methodId(REQUEST_RESPONSE_HANDLING_EXAMPLE)
                    //.requestMapper(toCaseNotificationRequest)
                    //.responseMapper(FromCaseNotificationResponse)
                    .build())
            .listeningPath(configString(SOAP_SECTION, EXAMPLE_SOAP_SERVICE_PATH))
            .serviceUrl(configString(HTTP_SERVER_SECTION_ID, URL) + configString(SOAP_SECTION, EXAMPLE_SOAP_SERVICE_PATH))
            .wsdlResourcePath(configString(SOAP_SECTION, EXAMPLE_SOAP_SERVICE_PATH) + WSDL_EXTENSION)
            .build();


    private final List<String> serviceTypes = fixedArrayOf(GRPC_SERVICE_TYPE, HTTP_SERVICE_TYPE, SOAP_SERVICE_TYPE);

    @Override
    public List<ServiceRequestInterceptor> getRequestInterceptors() {
        List<ServiceRequestInterceptor> interceptors = linkedListOf(serviceModule().getRequestInterceptors());
        interceptors.add(interceptRequest(new ExampleServiceInterception()));
        return interceptors;
    }

    @Override
    public List<ServiceResponseInterceptor> getResponseInterceptors() {
        List<ServiceResponseInterceptor> interceptors = linkedListOf(serviceModule().getResponseInterceptors());
        interceptors.add(interceptResponse(new ExampleServiceInterception()));
        return interceptors;
    }

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case REQUEST_RESPONSE_HANDLING_EXAMPLE:
                return cast(requestResponseHandlingExample((ExampleRequest) request));
            case SOAP_CLIENT_EXAMPLE:
                soapClientExample();
                return null;
            case USING_CONFIGURATION_VALUES_EXAMPLE:
                usingConfigurationValuesExample();
                return null;
            case HTTP_CLIENT_EXAMPLE:
                httpClientExample();
                return null;
            case GRPC_CLIENT_EXAMPLE:
                protobufClientExample();
                return null;
            case SQL_EXAMPLE:
                sqlExample();
                return null;
            case ROCKS_DB_EXAMPLE:
                rocksDbExample();
                return null;
            case LOGGING_EXAMPLE:
                loggingExample();
                return null;
            case JSON_READ_WRITE_EXAMPLE:
                jsonReadWriteExample();
                return null;
            case PROTOBUF_READ_WRITE_EXAMPLE:
                protobufReadWriteExample();
                return null;
            case ASYNC_TASK_EXECUTING_EXAMPLE:
                asyncTaskExecutingExample();
                return null;
            case GET_EXAMPLE_MODULE_STATE:
                return cast(getExampleModuleState());
            default:
                throw new UnknownServiceMethodException(serviceId, methodId);
        }
    }

}
