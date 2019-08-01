package ru.adk.http.server.specification;

import lombok.Getter;
import ru.adk.entity.mapper.ValueToModelMapper.StringParametersMapToModelMapper;
import ru.adk.http.server.model.HttpService;
import ru.adk.service.ServiceLoggingInterception;
import ru.adk.service.exception.UnknownServiceMethodException;
import ru.adk.service.interceptor.ServiceExecutionInterceptor.ServiceResponseInterceptor;
import ru.adk.service.model.ServiceInterceptionResult;
import ru.adk.service.model.ServiceRequest;
import ru.adk.service.model.ServiceResponse;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import static ru.adk.core.factory.CollectionsFactory.linkedListOf;
import static ru.adk.entity.CollectionValuesFactory.byteCollection;
import static ru.adk.entity.PrimitiveMapping.stringMapper;
import static ru.adk.http.constants.MimeToContentTypeMapper.imagePng;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.HttpParameters.RESOURCE;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.HttpPath.IMAGE_PATH;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.Methods.IMAGE;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.Methods.RENDER;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.SERVICE_ID;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.WEB_RESOURCE;
import static ru.adk.http.server.extractor.HttpWebResponseContentTypeExtractor.extractTypeByFile;
import static ru.adk.http.server.interceptor.HttpServerInterception.interceptAndContinue;
import static ru.adk.http.server.interceptor.HttpServerInterceptor.intercept;
import static ru.adk.http.server.model.HttpService.httpService;
import static ru.adk.http.server.module.HttpServerModule.httpServerModule;
import static ru.adk.http.server.service.HttpWebResourceService.getBinaryResource;
import static ru.adk.http.server.service.HttpWebResourceService.getStringResource;
import static ru.adk.service.interceptor.ServiceExecutionInterceptor.interceptResponse;
import static ru.adk.service.model.ServiceInterceptionResult.nextInterceptor;
import java.util.List;

@Getter
public class HttpWebUiServiceSpecification implements HttpServiceSpecification {
    private final String serviceId = SERVICE_ID;
    private final HttpService httpService = httpService()

            .get(RENDER)
            .fromPathParameters(RESOURCE)
            .requestMapper((StringParametersMapToModelMapper<String>) value -> value.getParameter(RESOURCE))
            .overrideResponseContentType()
            .responseMapper(stringMapper.getFromModel())
            .addRequestInterceptor(intercept(interceptAndContinue(((request, response) -> response.setContentType(extractTypeByFile(request.getRequestURI()))))))
            .listen(httpServerModule().getPath())

            .get(IMAGE)
            .fromPathParameters(RESOURCE)
            .requestMapper((StringParametersMapToModelMapper<String>) value -> value.getParameter(RESOURCE))
            .produces(imagePng())
            .ignoreRequestAcceptType()
            .responseMapper(image -> byteCollection((byte[]) image))
            .listen(httpServerModule().getPath() + IMAGE_PATH)

            .serve(EMPTY_STRING);

    @Override
    public List<ServiceResponseInterceptor> getResponseInterceptors() {
        return linkedListOf(interceptResponse(new ServiceLoggingInterception() {
            @Override
            public ServiceInterceptionResult intercept(ServiceRequest<?> request, ServiceResponse<?> response) {
                super.intercept(request, ServiceResponse.builder()
                        .command(response.getCommand())
                        .serviceException(response.getServiceException())
                        .responseData(WEB_RESOURCE)
                        .build());
                return nextInterceptor(request, response);
            }
        }));
    }

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case RENDER:
                return cast(getStringResource(cast(request)));
            case IMAGE:
                return cast(getBinaryResource(cast(request)));
        }
        throw new UnknownServiceMethodException(serviceId, methodId);
    }
}
