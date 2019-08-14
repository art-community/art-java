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

package ru.art.http.server.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.art.entity.mapper.ValueToModelMapper.StringParametersMapToModelMapper;
import ru.art.http.server.model.HttpService;
import ru.art.service.ServiceLoggingInterception;
import ru.art.service.exception.UnknownServiceMethodException;
import ru.art.service.interceptor.ServiceExecutionInterceptor.ResponseInterceptor;
import ru.art.service.model.ServiceInterceptionResult;
import ru.art.service.model.ServiceRequest;
import ru.art.service.model.ServiceResponse;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.core.factory.CollectionsFactory.linkedListOf;
import static ru.art.entity.CollectionValuesFactory.byteCollection;
import static ru.art.entity.PrimitiveMapping.stringMapper;
import static ru.art.http.constants.MimeToContentTypeMapper.imagePng;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.HTTP_WEB_UI_SERVICE;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.HttpParameters.RESOURCE;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.Methods.IMAGE;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.Methods.RENDER;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.WEB_RESOURCE;
import static ru.art.http.server.extractor.HttpWebResponseContentTypeExtractor.extractTypeByFile;
import static ru.art.http.server.interceptor.HttpServerInterception.interceptAndContinue;
import static ru.art.http.server.interceptor.HttpServerInterceptor.intercept;
import static ru.art.http.server.model.HttpService.httpService;
import static ru.art.http.server.service.HttpWebResourceService.getBinaryResource;
import static ru.art.http.server.service.HttpWebResourceService.getStringResource;
import static ru.art.service.interceptor.ServiceExecutionInterceptor.interceptResponse;
import static ru.art.service.model.ServiceInterceptionResult.nextInterceptor;
import java.util.List;

@Getter
@AllArgsConstructor
public class HttpWebUiServiceSpecification implements HttpServiceSpecification {
    private final String renderPath;
    private final String imagePath;
    private final String serviceId = HTTP_WEB_UI_SERVICE;
    @Getter(lazy = true)
    private final HttpService httpService = httpService()

            .get(RENDER)
            .fromPathParameters(RESOURCE)
            .requestMapper((StringParametersMapToModelMapper<String>) resource -> resource.getParameter(RESOURCE))
            .overrideResponseContentType()
            .responseMapper(stringMapper.getFromModel())
            .addRequestInterceptor(intercept(interceptAndContinue(((request, response) -> response.setContentType(extractTypeByFile(request.getRequestURI()))))))
            .listen(renderPath)

            .get(IMAGE)
            .fromPathParameters(RESOURCE)
            .requestMapper((StringParametersMapToModelMapper<String>) image -> image.getParameter(RESOURCE))
            .produces(imagePng())
            .ignoreRequestAcceptType()
            .responseMapper(image -> byteCollection((byte[]) image))
            .listen(imagePath)

            .serve(EMPTY_STRING);

    @Override
    public List<ResponseInterceptor> getResponseInterceptors() {
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
