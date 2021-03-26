/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.http.router;

import io.art.core.mime.*;
import io.art.core.model.*;
import io.art.http.configuration.HttpServerConfiguration;
import io.art.http.configuration.*;
import io.art.http.exception.*;
import io.art.http.model.*;
import io.art.http.state.*;
import io.art.server.specification.*;
import io.art.value.constants.ValueModuleConstants.*;
import io.netty.buffer.*;
import io.netty.handler.codec.http.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import reactor.netty.http.server.*;

import static io.art.core.mime.MimeTypes.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.http.constants.HttpModuleConstants.ExceptionMessages.*;
import static io.art.http.constants.HttpModuleConstants.HttpMethodNames.*;
import static io.art.http.module.HttpModule.*;
import static io.art.http.payload.HttpPayloadReader.*;
import static io.art.http.payload.HttpPayloadWriter.*;
import static io.art.server.module.ServerModule.*;
import static io.art.value.constants.ValueModuleConstants.DataFormat.*;
import static io.art.value.mime.MimeTypeDataFormatMapper.*;
import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static java.text.MessageFormat.*;

import java.util.*;
import java.util.concurrent.atomic.*;

public class HttpRouter {
    private final HttpModuleState moduleState = httpModule().state();

    public HttpRouter(HttpServerRoutes routes, HttpServerConfiguration configuration) {
        for (Map.Entry<String, HttpServiceConfiguration> service : configuration.getServices().entrySet()) {
            for (Map.Entry<String, HttpMethodConfiguration> method : service.getValue().getMethods().entrySet()) {
                HttpMethodConfiguration methodValue = method.getValue();
                HttpMethod httpMethod = methodValue.getMethod();
                switch (httpMethod.toString()){
                    case GET_METHOD_NAME:
                        routes.get(methodValue.getPath(), (request, response) -> handle(findSpecification(serviceMethod(service.getKey(), method.getKey())), request, response));
                        break;
                    case POST_METHOD_NAME:
                        routes.post(methodValue.getPath(), (request, response) -> handle(findSpecification(serviceMethod(service.getKey(), method.getKey())), request, response));
                        break;
                    case PUT_METHOD_NAME:
                        routes.put(methodValue.getPath(), (request, response) -> handle(findSpecification(serviceMethod(service.getKey(), method.getKey())), request, response));
                        break;
                    case DELETE_METHOD_NAME:
                        routes.delete(methodValue.getPath(), (request, response) -> handle(findSpecification(serviceMethod(service.getKey(), method.getKey())), request, response));
                        break;
                    case OPTIONS_METHOD_NAME:
                        routes.options(methodValue.getPath(), (request, response) -> handle(findSpecification(serviceMethod(service.getKey(), method.getKey())), request, response));
                        break;
                    case HEAD_METHOD_NAME:
                        routes.head(methodValue.getPath(), (request, response) -> handle(findSpecification(serviceMethod(service.getKey(), method.getKey())), request, response));
                        break;
                }
            }
        }
    }

    private Publisher<Void> handle(ServiceMethodSpecification specification, HttpServerRequest request, HttpServerResponse response) {
        HttpHeaders headers = request.requestHeaders();
        String contentType = headers.get(CONTENT_TYPE);
        String acceptType = headers.get(ACCEPT);
        DataFormat inputDataFormat = fromMimeType(MimeType.valueOf(contentType, TEXT_HTML), JSON);
        DataFormat outputDataFormat = fromMimeType(MimeType.valueOf(acceptType, TEXT_HTML), JSON);
        AtomicInteger status = new AtomicInteger(200);

        Sinks.Many<ByteBuf> unicast = Sinks.many().unicast().onBackpressureBuffer();

        return request.receive()
                .doFirst(() -> httpContextFrom(request, response))
                .map(content -> readPayloadData(inputDataFormat, content))
                .map(HttpPayloadValue::getValue)
                .transform(specification::serve)
                .map(output -> writePayloadData(outputDataFormat, output))
                .contextWrite(ctx -> ctx.put(HttpContext.class, HttpContext.from(request, response)))
                .doOnNext(unicast::tryEmitNext)
                .doOnError(unicast::tryEmitError)
                .doOnComplete(unicast::tryEmitComplete)
                .thenMany(response.send(unicast.asFlux()));
    }

    private ServiceMethodSpecification findSpecification(ServiceMethodIdentifier serviceMethodId) {
        return specifications()
                .findMethodById(serviceMethodId)
                .orElseThrow(() -> new HttpException(format(SPECIFICATION_NOT_FOUND, serviceMethodId)));
    }
}
