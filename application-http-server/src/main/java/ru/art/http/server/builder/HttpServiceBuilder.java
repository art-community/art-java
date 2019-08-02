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

package ru.art.http.server.builder;

import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.http.constants.MimeToContentTypeMapper;
import ru.art.http.server.interceptor.HttpServerInterceptor;
import ru.art.http.server.model.HttpService;
import ru.art.service.constants.RequestValidationPolicy;

public interface HttpServiceBuilder {
    HttpMethodWithBodyBuilder get(String methodId);

    HttpMethodWithBodyBuilder post(String methodId);

    HttpMethodWithBodyBuilder put(String methodId);

    HttpMethodWithBodyBuilder patch(String methodId);

    HttpMethodWithBodyBuilder delete(String methodId);

    HttpMethodWithParamsBuilder head(String methodId);

    HttpMethodWithParamsBuilder trace(String methodId);

    HttpMethodWithParamsBuilder connect(String methodId);

    HttpMethodWithBodyBuilder options(String methodId);

    HttpServiceBuilder addRequestInterceptor(HttpServerInterceptor interceptor);

    HttpServiceBuilder addResponseInterceptor(HttpServerInterceptor interceptor);

    HttpService serve(String serviceContextPath);

    interface HttpMethodBuilder {
        HttpServiceBuilder listen(String methodContextPath);

        HttpServiceBuilder listen();

        HttpMethodBuilder addRequestInterceptor(HttpServerInterceptor interceptor);

        HttpMethodBuilder addResponseInterceptor(HttpServerInterceptor interceptor);

        HttpMethodBuilder exceptionMapper(ValueFromModelMapper exceptionMapper);
    }

    interface HttpMethodWithParamsBuilder extends HttpMethodBuilder, HttpMethodResponseBuilder {
        HttpMethodRequestBuilder fromQueryParameters();

        HttpMethodRequestBuilder fromPathParameters(String... parameters);
    }

    interface HttpMethodWithBodyBuilder extends HttpMethodWithParamsBuilder {
        HttpMethodRequestBuilder fromBody();

        HttpMethodRequestBuilder fromMultipart();

        HttpMethodWithBodyBuilder consumes(MimeToContentTypeMapper mimeType);

        HttpMethodWithBodyBuilder ignoreRequestContentType();
    }

    interface HttpMethodRequestBuilder extends HttpMethodBuilder {
        HttpMethodResponseBuilder requestMapper(ValueToModelMapper requestMapper);

        HttpMethodRequestBuilder validationPolicy(RequestValidationPolicy policy);
    }

    interface HttpMethodResponseBuilder extends HttpMethodBuilder {
        HttpMethodBuilder responseMapper(ValueFromModelMapper responseMapper);

        HttpMethodResponseBuilder produces(MimeToContentTypeMapper mimeType);

        HttpMethodResponseBuilder ignoreRequestAcceptType();

        HttpMethodResponseBuilder overrideResponseContentType();
    }
}