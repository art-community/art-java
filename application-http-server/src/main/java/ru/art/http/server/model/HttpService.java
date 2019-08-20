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

package ru.art.http.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.art.entity.Value;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.http.constants.HttpMethodType;
import ru.art.http.constants.HttpRequestDataSource;
import ru.art.http.constants.MimeToContentTypeMapper;
import ru.art.http.server.builder.HttpServiceBuilder;
import ru.art.http.server.builder.HttpServiceBuilderImplementation;
import ru.art.http.server.constants.HttpServerModuleConstants.HttpResponseHandlingMode;
import ru.art.http.server.interceptor.HttpServerInterceptor;
import ru.art.http.server.path.HttpPath;
import ru.art.service.constants.RequestValidationPolicy;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class HttpService {
    private final String path;
    private final List<HttpMethod> httpMethods;
    private final List<HttpServerInterceptor> requestInterceptors;
    private final List<HttpServerInterceptor> responseInterceptors;

    public static HttpServiceBuilder httpService() {
        return new HttpServiceBuilderImplementation();
    }

    @Getter
    @Builder
    public static class HttpMethod {
        private final String methodId;
        private final HttpPath path;
        private final HttpMethodType methodType;
        private final HttpRequestDataSource requestDataSource;
        private final ValueToModelMapper requestMapper;
        private final ValueFromModelMapper responseMapper;
        private final ValueFromModelMapper exceptionMapper;
        private final List<ValueInterceptor<Value, Value>> requestValueInterceptors;
        private final List<ValueInterceptor<Value, Value>> responseValueInterceptors;
        private final List<ValueInterceptor<Value, Value>> exceptionValueInterceptors;
        private final List<HttpServerInterceptor> requestInterceptors;
        private final List<HttpServerInterceptor> responseInterceptors;
        private final RequestValidationPolicy requestValidationPolicy;
        private final MimeToContentTypeMapper consumesMimeType;
        private final MimeToContentTypeMapper producesMimeType;
        private final boolean ignoreRequestAcceptType;
        private final boolean ignoreRequestContentType;
        private final boolean overrideResponseContentType;
        private final HttpResponseHandlingMode responseHandlingMode;
    }

    @Getter
    @Builder
    public static class HttpMethodGroup {
        private Map<HttpMethodType, HttpMethod> methods;
    }
}